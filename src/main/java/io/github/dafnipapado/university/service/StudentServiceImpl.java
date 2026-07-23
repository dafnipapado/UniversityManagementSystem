package io.github.dafnipapado.university.service;

import io.github.dafnipapado.university.dto.student.StudentEditDTO;
import io.github.dafnipapado.university.dto.student.StudentInsertDTO;
import io.github.dafnipapado.university.dto.student.StudentReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.mapper.Mapper;
import io.github.dafnipapado.university.model.Student;
import io.github.dafnipapado.university.model.User;
import io.github.dafnipapado.university.model.UserInfo;
import io.github.dafnipapado.university.model.static_data.Region;
import io.github.dafnipapado.university.repository.RegionRepository;
import io.github.dafnipapado.university.repository.StudentRepository;
import io.github.dafnipapado.university.repository.UserInfoRepository;
import io.github.dafnipapado.university.repository.UserRepository;
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
public class StudentServiceImpl implements IStudentService{

    private final IUtilityService utilityService;
    private final StudentRepository studentRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public StudentReadOnlyDTO getIndex() throws EntityNotFoundException {
        return mapper.mapToStudentReadOnlyDTO(utilityService.getLoggedInStudent());
    }

    @Override
    @PreAuthorize("hasAuthority('INSERT_STUDENT')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public StudentReadOnlyDTO saveStudent(StudentInsertDTO studentInsertDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            if (studentInsertDTO.studentAM() != null && studentRepository.findByStudentAM(studentInsertDTO.studentAM()).isPresent()) {
                throw new EntityAlreadyExistsException("Student with AM = " + studentInsertDTO.studentAM() + " already exists.");
            }
            if (studentInsertDTO.userInfoInsertDTO().afm() != null && userInfoRepository.findByAfm(studentInsertDTO.userInfoInsertDTO().afm()).isPresent()) {
                throw new EntityAlreadyExistsException("User with afm = " + studentInsertDTO.userInfoInsertDTO().afm() + " already exists.");
            }
            if (studentInsertDTO.userInfoInsertDTO().email() != null && userInfoRepository.findByEmail(studentInsertDTO.userInfoInsertDTO().email()).isPresent()) {
                throw new EntityAlreadyExistsException("User with email = " + studentInsertDTO.userInfoInsertDTO().email() + " already exists.");
            }

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User savedUser = utilityService.getUserByUsername(username);

            //save student
            Student student = mapper.mapToStudentEntity(studentInsertDTO);
            student.addUser(savedUser);
            studentRepository.save(student);

            //save userInfo
            UserInfo userInfo = mapper.mapToUserInfoEntity(studentInsertDTO.userInfoInsertDTO());
            userInfo.addUser(savedUser);
            Region region = regionRepository.findById(studentInsertDTO.userInfoInsertDTO().regionId())
                    .orElseThrow(() -> new EntityNotFoundException("Region with id = " + studentInsertDTO.userInfoInsertDTO().regionId() + " not found."));
            region.addUserInfo(userInfo);
            userInfoRepository.save(userInfo);

            log.info("Student = {} {} was saved successfully.", student.getUser().getUserInfo().getFirstname(), student.getUser().getUserInfo().getLastname());
            return mapper.mapToStudentReadOnlyDTO(student);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Failed to save student with studentAM =  {}.", studentInsertDTO.studentAM());
            throw e;
        }
    }

    @Override
    public StudentEditDTO getStudentEditDTO(UUID uuid) throws EntityNotFoundException {
        Student student = studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Student with uuid " + uuid + " not found."));
        return mapper.mapToStudentEditDTO(student);
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_STUDENT')")
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public StudentReadOnlyDTO updateStudent(StudentEditDTO studentEditDTO) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            Student student = studentRepository.findByUuid(studentEditDTO.uuid())
                    .orElseThrow(() -> new EntityNotFoundException("Student with uuid = " + studentEditDTO.uuid() + " not found.}"));

            //checks for already existing studentAM, afm, email, if changed
            String updatedStudentAM = studentEditDTO.studentAM();
            if (!Objects.equals(updatedStudentAM, student.getStudentAM()) && studentRepository.findByStudentAM((updatedStudentAM)).isPresent()) {
                throw new EntityAlreadyExistsException("Student with AM = " + updatedStudentAM + " already exists.");
            }
            student.setStudentAM(updatedStudentAM);

            String updatedAfm = studentEditDTO.userInfoEditDTO().afm();
            if (!Objects.equals(updatedAfm, student.getUser().getUserInfo().getAfm()) && userInfoRepository.findByAfm(updatedAfm).isPresent()) {
                throw new EntityAlreadyExistsException("User with afm = " + updatedAfm + " already exists.");
            }
            student.getUser().getUserInfo().setAfm(updatedAfm);

            String updatedEmail = studentEditDTO.userInfoEditDTO().email();
            if (!Objects.equals(updatedEmail, student.getUser().getUserInfo().getEmail()) && userInfoRepository.findByEmail(updatedEmail).isPresent()) {
                throw new EntityAlreadyExistsException("User with email = " + updatedEmail + " already exists.");
            }
            student.getUser().getUserInfo().setEmail(updatedEmail);

            student.getUser().getUserInfo().setFirstname(studentEditDTO.userInfoEditDTO().firstname());
            student.getUser().getUserInfo().setLastname(studentEditDTO.userInfoEditDTO().lastname());
            student.getUser().getUserInfo().setTelephone(studentEditDTO.userInfoEditDTO().telephone());
            student.getUser().getUserInfo().setZipcode(studentEditDTO.userInfoEditDTO().zipCode());

            //get new region, if changed
            if (!Objects.equals(studentEditDTO.userInfoEditDTO().regionId(), student.getUser().getUserInfo().getRegion().getId())) {
                Region newRegion = regionRepository.findById(studentEditDTO.userInfoEditDTO().regionId())
                        .orElseThrow(() -> new EntityNotFoundException("Region with id = " + studentEditDTO.userInfoEditDTO().regionId() + " was not found"));
                newRegion.addUserInfo(student.getUser().getUserInfo());
            }

            //check for already existing username, if changed
            String updatedUsername = studentEditDTO.userEditDTO().username();
            if (!Objects.equals(updatedUsername, student.getUser().getUsername()) && userRepository.findByUsername(updatedUsername).isPresent()) {
                throw new EntityAlreadyExistsException("User with username = " + updatedUsername + " already exists.");
            }
            student.getUser().setUsername(updatedUsername);

            student.getUser().setPassword(passwordEncoder.encode(studentEditDTO.userEditDTO().password()));

            //update entities
            userRepository.save(student.getUser());
            userInfoRepository.save(student.getUser().getUserInfo());
            studentRepository.save(student);

            log.info("Student = {} {} was updated successfully.", student.getUser().getUserInfo().getFirstname(), student.getUser().getUserInfo().getLastname());
            return mapper.mapToStudentReadOnlyDTO(student);

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Failed to update student with uuid = {}.", studentEditDTO.uuid());
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_STUDENT')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteStudent(UUID uuid) throws EntityNotFoundException {
        try{
            Student student = studentRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Student with uuid = " + uuid + " was not found."));
            student.getUser().softDelete();
            student.softDelete();
        } catch (EntityNotFoundException e) {
            log.error("Failed to soft delete student with uuid = {}.", uuid);
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<StudentReadOnlyDTO> getStudentsPaginated(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        log.info("Paginated students fetched successfully with page = {} and size = {}", studentPage.getNumber(), studentPage.getSize());
        return studentPage.map(mapper::mapToStudentReadOnlyDTO);
    }
}
