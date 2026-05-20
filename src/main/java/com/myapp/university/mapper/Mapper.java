package com.myapp.university.mapper;

import com.myapp.university.dto.RegionReadOnlyDTO;
import com.myapp.university.dto.TeacherEditDTO;
import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.model.Teacher;
import com.myapp.university.model.User;
import com.myapp.university.model.UserInfo;
import com.myapp.university.model.static_data.Region;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public Teacher mapToTeacherEntity(TeacherInsertDTO dto) {
        return new Teacher(null, dto.teacherAM(), null, null, null);
    }

    public User mapToUserTeacherEntity(TeacherInsertDTO dto) {
        return new User(null, dto.username(), dto.password(), null, null, null, null);
    }

    public UserInfo mapToUserInfoTeacherEntity(TeacherInsertDTO dto) {
        return new UserInfo(null, dto.firstname(), dto.lastname(), dto.afm(), dto.email(), dto.telephone(), dto.zipCode(), null, null);
    }

    public TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getUser().getUserInfo().getFirstname(), teacher.getUser().getUserInfo().getLastname(), teacher.getTeacherAM());
    }

    public TeacherEditDTO mapToTeacherEditDTO(Teacher teacher) {
        return new TeacherEditDTO(teacher.getUuid(),
                teacher.getTeacherAM(),
                teacher.getUser().getUserInfo().getFirstname(),
                teacher.getUser().getUserInfo().getLastname(),
                teacher.getUser().getUserInfo().getAfm(),
                teacher.getUser().getUserInfo().getEmail(),
                teacher.getUser().getUserInfo().getTelephone(),
                teacher.getUser().getUserInfo().getZipcode(),
                teacher.getUser().getUserInfo().getRegion().getId(),
                teacher.getUser().getUsername(),
                teacher.getUser().getPassword()
                );
    }

    public RegionReadOnlyDTO mapToRegionReadOnlyDTO(Region region) {
        return new RegionReadOnlyDTO(region.getId(), region.getName());
    }
}
