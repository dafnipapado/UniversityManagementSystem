package io.github.dafnipapado.university.mapper;

import io.github.dafnipapado.university.dto.*;
import io.github.dafnipapado.university.dto.teacher.TeacherEditDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherInsertDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;
import io.github.dafnipapado.university.dto.user.UserEditDTO;
import io.github.dafnipapado.university.dto.user.UserInsertDTO;
import io.github.dafnipapado.university.dto.user.UserReadOnlyDTO;
import io.github.dafnipapado.university.dto.userInfo.UserInfoEditDTO;
import io.github.dafnipapado.university.model.Teacher;
import io.github.dafnipapado.university.model.User;
import io.github.dafnipapado.university.model.UserInfo;
import io.github.dafnipapado.university.model.static_data.Region;
import io.github.dafnipapado.university.model.static_data.Role;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public Teacher mapToTeacherEntity(TeacherInsertDTO dto) {
        return new Teacher(null, dto.teacherAM(), null, null, null);
    }

    public UserInfo mapToUserInfoEntity(TeacherInsertDTO dto) {
        return new UserInfo(
                null,
                dto.userInfoInsertDTO().firstname(),
                dto.userInfoInsertDTO().lastname(),
                dto.userInfoInsertDTO().afm(),
                dto.userInfoInsertDTO().email(),
                dto.userInfoInsertDTO().telephone(),
                dto.userInfoInsertDTO().zipCode(),
                null,
                null);
    }

    public TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getUuid().toString(), teacher.getUser().getUserInfo().getFirstname(), teacher.getUser().getUserInfo().getLastname(), teacher.getTeacherAM());
    }

    public TeacherEditDTO mapToTeacherEditDTO(Teacher teacher) {
        return new TeacherEditDTO(
                teacher.getUuid(),
                teacher.getTeacherAM(),
                new UserEditDTO(
                        teacher.getUser().getUsername(),
                        teacher.getUser().getPassword()
                ),
                new UserInfoEditDTO(
                        teacher.getUser().getUserInfo().getFirstname(),
                        teacher.getUser().getUserInfo().getLastname(),
                        teacher.getUser().getUserInfo().getAfm(),
                        teacher.getUser().getUserInfo().getEmail(),
                        teacher.getUser().getUserInfo().getTelephone(),
                        teacher.getUser().getUserInfo().getZipcode(),
                        teacher.getUser().getUserInfo().getRegion().getId()
                )
                );
    }

    public RegionReadOnlyDTO mapToRegionReadOnlyDTO(Region region) {
        return new RegionReadOnlyDTO(region.getId(), region.getName());
    }

    public RoleReadOnlyDTO mapToRoleReadOnlyDTO(Role role) {
        return new RoleReadOnlyDTO(role.getId(), role.getName());
    }

    public User mapToUserEntity(UserInsertDTO userInsertDTO){
        return new User(null, userInsertDTO.username(), userInsertDTO.password(), null, null, null, null);
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getUsername(), user.getRole().getName());
    }
}
