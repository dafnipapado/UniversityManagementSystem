package com.myapp.university.service;

import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.exception.UserAlreadyExistsException;
import com.myapp.university.mapper.Mapper;
import com.myapp.university.model.static_data.Role;
import com.myapp.university.model.Teacher;
import com.myapp.university.model.User;
import com.myapp.university.model.UserInfo;
import com.myapp.university.model.static_data.Region;
import com.myapp.university.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO) throws UserAlreadyExistsException {
        try {
            log.warn("First" + teacherInsertDTO.username());
            System.out.println("In the service");
            if (teacherInsertDTO.teacherAM() != null && teacherRepository.findByTeacherAM(teacherInsertDTO.teacherAM()).isPresent()) {
                throw new UserAlreadyExistsException("");
            }

            Long teacherId = 2L;

            log.warn(teacherInsertDTO.username());
            log.warn(teacherInsertDTO.password());
            log.warn(teacherInsertDTO.firstname());
            log.warn(teacherInsertDTO.lastname());
            log.warn(teacherInsertDTO.teacherAM());
            log.warn(teacherInsertDTO.afm());
            log.warn(teacherInsertDTO.email());
            log.warn(teacherInsertDTO.telephone());
            log.warn(teacherInsertDTO.zipCode());
            System.out.println(teacherInsertDTO.regionId());
            System.out.println(teacherInsertDTO.roleId());
            //save user
            User user = mapper.mapToUserTeacherEntity(teacherInsertDTO);

            log.warn("User to be saved: " + user.getUsername());
            Role role = roleRepository.findById(teacherId).orElseThrow();
            log.warn("Role to be assigned: " + role.getName());
            role.addUser(user);
            log.error("User to be saved: " + user.getUsername() + user.getRole().getName());

//            log.error("User to be saved: " + user.getUsername());
//            log.warn("User to be saved (dto): " + teacherInsertDTO.username());
            User savedUser = userRepository.save(user);
            log.warn("User saved: " + savedUser.getUsername());

            //get user's id
            Long insertedUserId = savedUser.getId();
//            log.error("Inserted user id: " + insertedUserId);

            //save userInfo
            UserInfo userInfo = mapper.mapToUserInfoTeacherEntity(teacherInsertDTO);
            userInfo.setUser(userRepository.findById(insertedUserId).orElseThrow());
            Region region = regionRepository.findById((long) teacherInsertDTO.regionId()).orElseThrow();
            region.saveUserInfo(userInfo);
            userInfoRepository.save(userInfo);

            //save teacher
            Teacher teacher = mapper.mapToTeacherEntity(teacherInsertDTO);
            teacher.setUser(userRepository.findById(insertedUserId).orElseThrow());
            teacherRepository.save(teacher);

            //return teacherReadOnlyDTO
            return mapper.mapToTeacherReadOnlyDTO(teacher, userInfo);

        } catch (UserAlreadyExistsException e) {
            log.error("ERROR!!!");
            throw e;
        }
    }
}
