package io.github.dafnipapado.university.mapper;

import io.github.dafnipapado.university.dto.*;
import io.github.dafnipapado.university.dto.course.CourseEditDTO;
import io.github.dafnipapado.university.dto.course.CourseInsertDTO;
import io.github.dafnipapado.university.dto.course.CourseReadOnlyDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingEditDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingReadOnlyDTO;
import io.github.dafnipapado.university.dto.semester.SemesterInsertDTO;
import io.github.dafnipapado.university.dto.semester.SemesterReadOnlyDTO;
import io.github.dafnipapado.university.dto.student.StudentEditDTO;
import io.github.dafnipapado.university.dto.student.StudentInsertDTO;
import io.github.dafnipapado.university.dto.student.StudentReadOnlyDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherEditDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherInsertDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;
import io.github.dafnipapado.university.dto.user.UserEditDTO;
import io.github.dafnipapado.university.dto.user.UserInsertDTO;
import io.github.dafnipapado.university.dto.user.UserReadOnlyDTO;
import io.github.dafnipapado.university.dto.userInfo.UserInfoEditDTO;
import io.github.dafnipapado.university.dto.userInfo.UserInfoInsertDTO;
import io.github.dafnipapado.university.model.*;
import io.github.dafnipapado.university.model.static_data.Department;
import io.github.dafnipapado.university.model.static_data.Region;
import io.github.dafnipapado.university.model.static_data.Role;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public Teacher mapToTeacherEntity(TeacherInsertDTO dto) {
        return new Teacher(null, dto.teacherAM(), null, null, null);
    }

    public UserInfo mapToUserInfoEntity(UserInfoInsertDTO dto) {
        return new UserInfo(
                null,
                dto.firstname(),
                dto.lastname(),
                dto.afm(),
                dto.email(),
                dto.telephone(),
                dto.zipCode(),
                null,
                null);
    }

    public TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(
                teacher.getUuid(),
                teacher.getUser().getUserInfo().getFirstname(),
                teacher.getUser().getUserInfo().getLastname(),
                teacher.getTeacherAM());
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

    public StudentReadOnlyDTO mapToStudentReadOnlyDTO(Student student) {
        return new StudentReadOnlyDTO(
                student.getUuid().toString(),
                student.getUser().getUserInfo().getFirstname(),
                student.getUser().getUserInfo().getLastname(),
                student.getStudentAM());
    }

    public Student mapToStudentEntity(StudentInsertDTO studentInsertDTO) {
        return new Student(null, studentInsertDTO.studentAM(), null, null, null, null);
    }

    public StudentEditDTO mapToStudentEditDTO(Student student) {
        return new StudentEditDTO(
                student.getUuid(),
                student.getStudentAM(),
                new UserEditDTO(
                        student.getUser().getUsername(),
                        student.getUser().getPassword()
                ),
                new UserInfoEditDTO(
                        student.getUser().getUserInfo().getFirstname(),
                        student.getUser().getUserInfo().getLastname(),
                        student.getUser().getUserInfo().getAfm(),
                        student.getUser().getUserInfo().getEmail(),
                        student.getUser().getUserInfo().getTelephone(),
                        student.getUser().getUserInfo().getZipcode(),
                        student.getUser().getUserInfo().getRegion().getId()
                )
        );
    }

    public Course mapToCourseEntity(CourseInsertDTO courseInsertDTO) {
        return new Course(
                null,
                null,
                courseInsertDTO.code(),
                courseInsertDTO.name(),
                courseInsertDTO.description(),
                courseInsertDTO.ects(),
                null,
                null);
    }

    public CourseReadOnlyDTO mapToCourseReadOnlyDTO(Course course) {
        return new CourseReadOnlyDTO(
                course.getUuid(),
                course.getCode(),
                course.getName(),
                course.getDescription(),
                course.getEcts(),
                course.getDepartment().getName()
        );
    }

    public CourseEditDTO mapToCourseEditDTO(Course course) {
        return new CourseEditDTO(
                course.getUuid(),
                course.getCode(),
                course.getName(),
                course.getDescription(),
                course.getEcts(),
                course.getDepartment().getId()
        );
    }

    public DepartmentReadOnlyDTO mapToDepartmentReadOnlyDTO(Department department) {
        return new DepartmentReadOnlyDTO(department.getId(), department.getName());
    }

    public Semester mapToSemesterEntity(SemesterInsertDTO semesterInsertDTO) {
        return new Semester(
                null,
                semesterInsertDTO.name(),
                semesterInsertDTO.year(),
                null
        );
    }

    public CourseOfferingReadOnlyDTO mapToCourseOfferingReadOnlyDTO(CourseOffering courseOffering) {
        return new CourseOfferingReadOnlyDTO(
                courseOffering.getUuid().toString(),
                mapToCourseReadOnlyDTO(courseOffering.getCourse()),
                mapToTeacherReadOnlyDTO(courseOffering.getTeacher()),
                new SemesterReadOnlyDTO(courseOffering.getSemester().getName(), courseOffering.getSemester().getYear())
        );
    }

    public CourseOfferingEditDTO mapToCourseOfferingEditDTO(CourseOffering courseOffering){
        return new CourseOfferingEditDTO(
                courseOffering.getUuid(),
                courseOffering.getCourse().getUuid(),
                courseOffering.getTeacher().getUuid(),
                courseOffering.getSemester().getId()
        );
    }

}
