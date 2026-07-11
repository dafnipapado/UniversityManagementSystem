package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.teacher.TeacherEditDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherInsertDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.Teacher;
import io.github.dafnipapado.university.model.User;
import io.github.dafnipapado.university.model.UserInfo;
import io.github.dafnipapado.university.model.static_data.Region;
import io.github.dafnipapado.university.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final UserInfoRepository userInfoRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public TeacherReadOnlyDTO getIndex() throws EntityNotFoundException {
        User user = getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Teacher teacher = user.getTeacher();
        return mapper.mapToTeacherReadOnlyDTO(teacher);
    }

    @Override
    @PreAuthorize("hasAuthority('INSERT_TEACHER')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            if (teacherInsertDTO.teacherAM() != null && teacherRepository.findByTeacherAM(teacherInsertDTO.teacherAM()).isPresent()) {
                throw new EntityAlreadyExistsException("Teacher with AM = " + teacherInsertDTO.teacherAM() + " already exists.");
            }

            Long teacherRoleId = 2L;

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User savedUser = getUserByUsername(username);

            //save teacher
            Teacher teacher = mapper.mapToTeacherEntity(teacherInsertDTO);
            teacher.addUser(savedUser);
            teacherRepository.save(teacher);

            //save userInfo
            UserInfo userInfo = mapper.mapToUserInfoEntity(teacherInsertDTO);
            userInfo.addUser(savedUser);
            Region region = regionRepository.findById(teacherInsertDTO.userInfoInsertDTO().regionId())
                    .orElseThrow(() -> new EntityNotFoundException("Region with id = {teacherInsertDTO.regionId()} not found."));
            region.addUserInfo(userInfo);
            userInfoRepository.save(userInfo);

            return mapper.mapToTeacherReadOnlyDTO(teacher);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public TeacherEditDTO findTeacherByUuid(UUID uuid) throws EntityNotFoundException {
        Teacher teacher = teacherRepository.findByUuid(uuid).orElseThrow();
        TeacherEditDTO teacherEditDTO = mapper.mapToTeacherEditDTO(teacher);
        return teacherEditDTO;
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_TEACHER')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            Teacher teacher = teacherRepository.findByUuid(teacherEditDTO.uuid()).orElseThrow(() -> new EntityNotFoundException("Teacher with uuid = {teacherEditDTO.uuid()} not found.}"));

            //check if teacherAM is changed - if yes, check if new teacherAM already exists
            if (!(teacherEditDTO.teacherAM().equals(teacher.getTeacherAM()))) {
                if (teacherRepository.findByTeacherAM((teacherEditDTO.teacherAM())).isPresent()) {
                    throw new EntityAlreadyExistsException("Teacher with AM = {teacherEditDTO.teacherAM()} already exists.");
                }
                teacher.setTeacherAM(teacherEditDTO.teacherAM());
            }

            teacher.getUser().getUserInfo().setFirstname(teacherEditDTO.userInfoEditDTO().firstname());
            teacher.getUser().getUserInfo().setLastname(teacherEditDTO.userInfoEditDTO().lastname());
            teacher.getUser().getUserInfo().setAfm(teacherEditDTO.userInfoEditDTO().afm());
            teacher.getUser().getUserInfo().setEmail(teacherEditDTO.userInfoEditDTO().email());
            teacher.getUser().getUserInfo().setTelephone(teacherEditDTO.userInfoEditDTO().telephone());
            teacher.getUser().getUserInfo().setZipcode(teacherEditDTO.userInfoEditDTO().zipCode());

            //check if region is changed
            if (!(teacherEditDTO.userInfoEditDTO().regionId().equals(teacher.getUser().getUserInfo().getRegion().getId()))) {
                Region newRegion = regionRepository.findById(teacherEditDTO.userInfoEditDTO().regionId())
                        .orElseThrow(() -> new EntityNotFoundException("Region with id = {teacherEditDTO.regionId()} was not found"));
                newRegion.addUserInfo(teacher.getUser().getUserInfo());
            }

            //check if username is changed - if yes, check if new username already exists
            if (!(teacherEditDTO.userEditDTO().username().equals(teacher.getUser().getUsername()))) {
                if (userRepository.findByUsername(teacherEditDTO.userEditDTO().username()).isPresent()) {
                    throw new EntityAlreadyExistsException("User with username = {teacherEditDTO.username()} already exists.");
                }
                teacher.getUser().setUsername(teacherEditDTO.userEditDTO().username());
            }

            teacher.getUser().setPassword(passwordEncoder.encode(teacherEditDTO.userEditDTO().password()));

            //update entities
            userRepository.save(teacher.getUser());
            userInfoRepository.save(teacher.getUser().getUserInfo());
            teacherRepository.save(teacher);

            return mapper.mapToTeacherReadOnlyDTO(teacher);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_TEACHER')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteTeacher(UUID uuid) throws EntityNotFoundException {
        try{
            Teacher teacher = teacherRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("Teacher with uuid = {uuid} was not found."));
            teacher.getUser().softDelete();
            teacher.softDelete();
        } catch (EntityNotFoundException e) {
            log.error("Deletion of teacher with uuid = {uuid} failed.");
            throw e;
        }

    }


    @Override
    public User getUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username = " + username + " doesn't exist"));

    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<TeacherReadOnlyDTO> getTeachersPaginated(Pageable pageable) {
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        log.debug("Get paginated return successfully page={} and size={}", teacherPage.getNumber(), teacherPage.getSize());
        return teacherPage.map(mapper::mapToTeacherReadOnlyDTO);
    }
}
