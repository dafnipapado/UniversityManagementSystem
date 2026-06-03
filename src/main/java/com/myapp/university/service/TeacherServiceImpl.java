package com.myapp.university.service;

import com.myapp.university.dto.TeacherEditDTO;
import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.exception.EntityAlreadyExistsException;
import com.myapp.university.exception.EntityNotFoundException;
import com.myapp.university.mapper.Mapper;
import com.myapp.university.model.static_data.Role;
import com.myapp.university.model.Teacher;
import com.myapp.university.model.User;
import com.myapp.university.model.UserInfo;
import com.myapp.university.model.static_data.Region;
import com.myapp.university.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
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

            Region region = regionRepository.findById(teacherInsertDTO.regionId()).orElseThrow();
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

            teacher.getUser().getUserInfo().setFirstname(teacherEditDTO.firstname());
            teacher.getUser().getUserInfo().setLastname(teacherEditDTO.lastname());
            teacher.getUser().getUserInfo().setAfm(teacherEditDTO.afm());
            teacher.getUser().getUserInfo().setEmail(teacherEditDTO.email());
            teacher.getUser().getUserInfo().setTelephone(teacherEditDTO.telephone());
            teacher.getUser().getUserInfo().setZipcode(teacherEditDTO.zipCode());

            //check if region is changed
            if (!(teacherEditDTO.regionId().equals(teacher.getUser().getUserInfo().getRegion().getId()))) {
                Region newRegion = regionRepository.findById(teacherEditDTO.regionId()).orElseThrow(() -> new EntityNotFoundException("Region with id = {teacherEditDTO.regionId()} was not found"));
                newRegion.addUserInfo(teacher.getUser().getUserInfo());
            }

            //check if username is changed - if yes, check if new username already exists
            if (!(teacherEditDTO.username().equals(teacher.getUser().getUsername()))) {
                if (userRepository.findByUsername(teacherEditDTO.username()).isPresent()) {
                    throw new EntityAlreadyExistsException("User with username = {teacherEditDTO.username()} already exists.");
                }
                teacher.getUser().setUsername(teacherEditDTO.username());
            }

            teacher.getUser().setPassword(passwordEncoder.encode(teacherEditDTO.password()));

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
    public List<TeacherReadOnlyDTO> viewTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacher -> {
                    UserInfo userInfo = userInfoRepository.findByUser(teacher.getUser()).orElseThrow();
                    return mapper.mapToTeacherReadOnlyDTO(teacher);
                })
                .toList();
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
