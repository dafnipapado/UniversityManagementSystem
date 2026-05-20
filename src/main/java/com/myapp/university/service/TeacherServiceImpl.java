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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements ITeacherService{

    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final UserInfoRepository userInfoRepository;
    private final Mapper mapper;

    @Override
    @Transactional (rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            if (teacherInsertDTO.teacherAM() != null && teacherRepository.findByTeacherAM(teacherInsertDTO.teacherAM()).isPresent()) {
                throw new EntityAlreadyExistsException("Teacher with AM = {teacherInsertDTO.teacherAM()} already exists.");
            }

            Long teacherRoleId = 2L;

            //save user
            User user = mapper.mapToUserTeacherEntity(teacherInsertDTO);

            Role role = roleRepository.findById(teacherRoleId).orElseThrow(() -> new EntityNotFoundException("Role with id = {teacherRoleId} was not found."));
            role.addUser(user);

            User savedUser = userRepository.save(user);

            //get user's id
            Long insertedUserId = savedUser.getId();

            //save userInfo
            UserInfo userInfo = mapper.mapToUserInfoTeacherEntity(teacherInsertDTO);
            userInfo.setUser(userRepository.findById(insertedUserId).orElseThrow());
            Region region = regionRepository.findById(teacherInsertDTO.regionId()).orElseThrow();
            region.saveUserInfo(userInfo);
            userInfoRepository.save(userInfo);

            //save teacher
            Teacher teacher = mapper.mapToTeacherEntity(teacherInsertDTO);
            teacher.setUser(userRepository.findById(insertedUserId).orElseThrow());
            teacherRepository.save(teacher);

            //return teacherReadOnlyDTO
            return mapper.mapToTeacherReadOnlyDTO(teacher, userInfo);

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
                    return mapper.mapToTeacherReadOnlyDTO(teacher, userInfo);
                })
                .toList();
    }
}
