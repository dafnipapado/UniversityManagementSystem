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

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements ITeacherService {

    private final IUserService userService;
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
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
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
            if (teacherInsertDTO.userInfoInsertDTO().afm() != null && userInfoRepository.findByAfm(teacherInsertDTO.userInfoInsertDTO().afm()).isPresent()) {
                throw new EntityAlreadyExistsException("User with afm = " + teacherInsertDTO.userInfoInsertDTO().afm() + " already exists.");
            }
            if (teacherInsertDTO.userInfoInsertDTO().email() != null && userInfoRepository.findByEmail(teacherInsertDTO.userInfoInsertDTO().email()).isPresent()) {
                throw new EntityAlreadyExistsException("User with email = " + teacherInsertDTO.userInfoInsertDTO().email() + " already exists.");
            }

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User savedUser = userService.getUserByUsername(username);

            //save teacher
            Teacher teacher = mapper.mapToTeacherEntity(teacherInsertDTO);
            teacher.addUser(savedUser);
            teacherRepository.save(teacher);

            //save userInfo
            UserInfo userInfo = mapper.mapToUserInfoEntity(teacherInsertDTO);
            userInfo.addUser(savedUser);
            Region region = regionRepository.findById(teacherInsertDTO.userInfoInsertDTO().regionId())
                    .orElseThrow(() -> new EntityNotFoundException("Region with id = " + teacherInsertDTO.userInfoInsertDTO().regionId() + " not found."));
            region.addUserInfo(userInfo);
            userInfoRepository.save(userInfo);

            log.info("Teacher = {} {} was saved successfully.", teacher.getUser().getUserInfo().getFirstname(), teacher.getUser().getUserInfo().getLastname());
            return mapper.mapToTeacherReadOnlyDTO(teacher);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Failed to save teacher with teacherAM =  {}.", teacherInsertDTO.teacherAM());
            throw e;
        }
    }

    @Override
    public TeacherEditDTO getTeacherEditDTO(UUID uuid) throws EntityNotFoundException {
        Teacher teacher = teacherRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with uuid " + uuid + " not found."));
        return mapper.mapToTeacherEditDTO(teacher);
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_TEACHER')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            Teacher teacher = teacherRepository.findByUuid(teacherEditDTO.uuid())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher with uuid = " + teacherEditDTO.uuid() + " not found.}"));

            //checks for already existing teacherAM, afm, email, if changed
            String updatedTeacherAM = teacherEditDTO.teacherAM();
            if (!Objects.equals(updatedTeacherAM, teacher.getTeacherAM()) && teacherRepository.findByTeacherAM((updatedTeacherAM)).isPresent()) {
                throw new EntityAlreadyExistsException("Teacher with AM = " + updatedTeacherAM + " already exists.");
            }
            teacher.setTeacherAM(updatedTeacherAM);

            String updatedAfm = teacherEditDTO.userInfoEditDTO().afm();
            if (!Objects.equals(updatedAfm, teacher.getUser().getUserInfo().getAfm()) && userInfoRepository.findByAfm(updatedAfm).isPresent()) {
                throw new EntityAlreadyExistsException("User with afm = " + updatedAfm + " already exists.");
            }
            teacher.getUser().getUserInfo().setAfm(updatedAfm);

            String updatedEmail = teacherEditDTO.userInfoEditDTO().email();
            if (!Objects.equals(updatedEmail, teacher.getUser().getUserInfo().getEmail()) && userInfoRepository.findByEmail(updatedEmail).isPresent()) {
                throw new EntityAlreadyExistsException("User with email = " + updatedEmail + " already exists.");
            }
            teacher.getUser().getUserInfo().setEmail(updatedEmail);

            teacher.getUser().getUserInfo().setFirstname(teacherEditDTO.userInfoEditDTO().firstname());
            teacher.getUser().getUserInfo().setLastname(teacherEditDTO.userInfoEditDTO().lastname());
            teacher.getUser().getUserInfo().setTelephone(teacherEditDTO.userInfoEditDTO().telephone());
            teacher.getUser().getUserInfo().setZipcode(teacherEditDTO.userInfoEditDTO().zipCode());

            //get new region, if changed
            if (!Objects.equals(teacherEditDTO.userInfoEditDTO().regionId(), teacher.getUser().getUserInfo().getRegion().getId())) {
                Region newRegion = regionRepository.findById(teacherEditDTO.userInfoEditDTO().regionId())
                        .orElseThrow(() -> new EntityNotFoundException("Region with id = " + teacherEditDTO.userInfoEditDTO().regionId() + " was not found"));
                newRegion.addUserInfo(teacher.getUser().getUserInfo());
            }

            //check for already existing username, if changed
            String updatedUsername = teacherEditDTO.userEditDTO().username();
            if (!Objects.equals(updatedUsername, teacher.getUser().getUsername()) && userRepository.findByUsername(updatedUsername).isPresent()) {
                throw new EntityAlreadyExistsException("User with username = " + updatedUsername + " already exists.");
            }
            teacher.getUser().setUsername(updatedUsername);

            teacher.getUser().setPassword(passwordEncoder.encode(teacherEditDTO.userEditDTO().password()));

            //update entities
            userRepository.save(teacher.getUser());
            userInfoRepository.save(teacher.getUser().getUserInfo());
            teacherRepository.save(teacher);

            log.info("Teacher = {} {} was updated successfully.", teacher.getUser().getUserInfo().getFirstname(), teacher.getUser().getUserInfo().getLastname());
            return mapper.mapToTeacherReadOnlyDTO(teacher);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Failed to update teacher with uuid = {}.", teacherEditDTO.uuid());
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_TEACHER')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteTeacher(UUID uuid) throws EntityNotFoundException {
        try{
            Teacher teacher = teacherRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher with uuid = " + uuid + " was not found."));
            teacher.getUser().softDelete();
            teacher.softDelete();
        } catch (EntityNotFoundException e) {
            log.error("Failed to soft delete teacher with uuid = {}.", uuid);
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<TeacherReadOnlyDTO> getTeachersPaginated(Pageable pageable) {
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        log.info("Paginated teachers fetched successfully with page = {} and size = {}", teacherPage.getNumber(), teacherPage.getSize());
        return teacherPage.map(mapper::mapToTeacherReadOnlyDTO);
    }
}
