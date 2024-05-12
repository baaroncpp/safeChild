package com.bwongo.core.student_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.dto.PageResponseDto;
import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.core_banking.service.MemberService;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.repository.SchoolRepository;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.student_mgt.model.dto.*;
import com.bwongo.core.student_mgt.model.jpa.TGuardian;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.model.jpa.TStudentGuardian;
import com.bwongo.core.student_mgt.repository.GuardianRepository;
import com.bwongo.core.student_mgt.repository.StudentGuardianRepository;
import com.bwongo.core.student_mgt.repository.StudentRepository;
import com.bwongo.core.user_mgt.repository.TUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.bwongo.core.base.utils.BaseUtils.pageToDto;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.SCHOOL_NOT_FOUND;
import static com.bwongo.core.student_mgt.utils.StudentManagementUtils.studentAlreadyHasNotifyingGuardian;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.*;
import static com.bwongo.core.vehicle_mgt.utils.VehicleMsgConstants.CANT_ASSIGN_SCHOOL;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final SchoolRepository schoolRepository;
    private final GuardianRepository guardianRepository;
    private final StudentRepository studentRepository;
    private final StudentGuardianRepository studentGuardianRepository;
    private final StudentDtoService studentDtoService;
    private final AuditService auditService;
    private final TUserRepository userRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final MemberService memberService;

    public List<StudentResponseDto> addStudents(List<StudentRequestDto> studentsRequestDto){

        return validateList0fStudentRequests(studentsRequestDto).stream()
                .map(studentDtoService::studentToDto)
                .toList();
    }

    @Transactional
    public StudentResponseDto addStudent(StudentRequestDto studentRequestDto){

        studentRequestDto.validate();
        final var nationalIdNumber = studentRequestDto.nationalIdNumber();
        final var schoolIdNumber = studentRequestDto.schoolIdNumber();
        final var schoolId = studentRequestDto.schoolId();
        final var studentEmail = studentRequestDto.email();

        var school = getSchool(schoolId);

        if(!studentEmail.isEmpty())
            Validate.isTrue(this, !studentRepository.existsByEmail(studentEmail), ExceptionType.BAD_REQUEST, EMAIL_ALREADY_TAKEN, studentEmail);

        if(!nationalIdNumber.isEmpty())
            Validate.isTrue(this, !studentRepository.existsByNationalIdNumber(nationalIdNumber), ExceptionType.BAD_REQUEST, NATIONAL_ID_ALREADY_TAKEN, nationalIdNumber);

        Validate.isTrue(this, !studentRepository.existsBySchoolIdNumberAndSchool(schoolIdNumber, school), ExceptionType.BAD_REQUEST, STUDENT_ID_ALREADY_TAKEN, schoolIdNumber);

        var student = studentDtoService.dtoToTStudent(studentRequestDto);
        student.setStudentUsername(getNonExistingStudentUsername());
        auditService.stampAuditedEntity(student);

        var existingEditingUser = userRepository.findById(student.getCreatedBy().getId());
        final var editingUser = existingEditingUser.get();

        if(!editingUser.getUserType().equals(UserTypeEnum.ADMIN))
            Validate.isTrue(this, schoolUserRepository.existsBySchoolAndUser(school, editingUser), ExceptionType.ACCESS_DENIED, CANT_ASSIGN_SCHOOL, schoolId);

        return studentDtoService.studentToDto(studentRepository.save(student));
    }

    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto){

        studentRequestDto.validate();

        var existingStudent = getStudent(id);
        final var nationalIdNumber = studentRequestDto.nationalIdNumber();
        final var schoolIdNumber = studentRequestDto.schoolIdNumber();
        final var schoolId = studentRequestDto.schoolId();

        var school = getSchool(schoolId);

        if(!nationalIdNumber.isEmpty() && !nationalIdNumber.equals(existingStudent.getNationalIdNumber()))
            Validate.isTrue(this, !studentRepository.existsByNationalIdNumber(nationalIdNumber), ExceptionType.BAD_REQUEST, NATIONAL_ID_ALREADY_TAKEN, nationalIdNumber);

        if(!schoolIdNumber.equals(existingStudent.getSchoolIdNumber()))
            Validate.isTrue(this, !studentRepository.existsBySchoolIdNumberAndSchool(schoolIdNumber, school), ExceptionType.BAD_REQUEST, STUDENT_ID_ALREADY_TAKEN, schoolIdNumber);

        var updatedStudent = studentDtoService.dtoToTStudent(studentRequestDto);
        auditService.stampAuditedEntity(updatedStudent);
        updatedStudent.setId(existingStudent.getId());

        var existingEditingUser = userRepository.findById(auditService.getLoggedInUser().getId());
        final var editingUser = existingEditingUser.get();

        if(!editingUser.getUserType().equals(UserTypeEnum.ADMIN))
            Validate.isTrue(this, schoolUserRepository.existsBySchoolAndUser(school, editingUser), ExceptionType.ACCESS_DENIED, CANT_ASSIGN_SCHOOL, schoolId);

        return studentDtoService.studentToDto(studentRepository.save(updatedStudent));
    }

    public StudentResponseDto getStudentById(Long id){
        return studentDtoService.studentToDto(getStudent(id));
    }

    public StudentResponseDto getStudentByStudentIdNumberAndSchoolId(String studentIdNumber, Long schoolId){

        final var school = getSchool(schoolId);

        var existingStudent = studentRepository.findByDeletedAndSchoolIdNumberAndSchool(Boolean.FALSE, studentIdNumber, school);
        Validate.isPresent(this, existingStudent, STUDENT_NOT_FOUND, studentIdNumber);
        final var student = existingStudent.get();

        return studentDtoService.studentToDto(student);
    }

    public PageResponseDto getAllStudents(Pageable pageable, Long schoolId){

        if(schoolId == null)
            return getAllStudentsAdmin(pageable);

        var school = getSchool(schoolId);

        var studentPage = studentRepository.findAllByDeletedAndSchool(pageable, Boolean.FALSE, school);

        var students = studentPage.stream()
                .map(studentDtoService::studentToDto)
                .toList();

        return pageToDto(studentPage, students);
    }

    public PageResponseDto getAllStudentsAdmin(Pageable pageable){
        var studentPage = studentRepository.findAllByDeleted(pageable, Boolean.FALSE);

        var students = studentPage.stream()
                .map(studentDtoService::studentToDto)
                .toList();

        return pageToDto(studentPage, students);
    }

    @Transactional
    public StudentGuardianDto addStudentGuardian(GuardianRequestDto guardianRequestDto){

        guardianRequestDto.validate();
        final var phoneNumber = guardianRequestDto.phoneNumber();
        final var studentId = guardianRequestDto.studentId();

        var existingStudent = getStudent(studentId);

        Validate.isTrue(this, !guardianRepository.existsByPhoneNumberAndDeleted(phoneNumber, Boolean.FALSE),
                ExceptionType.BAD_REQUEST,
                GUARDIAN_PHONE_TAKEN,
                phoneNumber);

        var guardian = studentDtoService.dtoToTGuardian(guardianRequestDto);
        auditService.stampAuditedEntity(guardian);

        var savedGuardian = guardianRepository.save(guardian);

        return getStudentGuardianDto(existingStudent, savedGuardian);
    }

    @Transactional
    public StudentGuardianDto assignGuardianToStudent(Long studentId, Long guardianId){

        var existingStudent = getStudent(studentId);
        var existingGuardian = getGuardian(guardianId);

        return getStudentGuardianDto(existingStudent, existingGuardian);
    }

    private StudentGuardianDto getStudentGuardianDto(TStudent existingStudent, TGuardian existingGuardian) {
        var studentGuardian = new TStudentGuardian();
        studentGuardian.setGuardian(existingGuardian);
        studentGuardian.setStudent(existingStudent);
        auditService.stampAuditedEntity(studentGuardian);

        var studentGuardians = studentGuardianRepository.findAllByStudent(existingStudent);

        if(existingGuardian.isNotified() && !studentAlreadyHasNotifyingGuardian(studentGuardians)) {
            var coreBankingId = memberService.addStudent(existingStudent, existingGuardian);
            existingStudent.setCoreBankingId(coreBankingId);
            studentRepository.save(existingStudent);
        }

        return studentDtoService.studentGuardianToDto(studentGuardianRepository.save(studentGuardian));
    }

    public List<StudentGuardianDto> getStudentsByGuardian(Long guardianId){

        var guardian = getGuardian(guardianId);

        return studentGuardianRepository.findAllByGuardian(guardian).stream()
                .map(studentDtoService::studentGuardianToDto)
                .toList();
    }

    public List<StudentGuardianDto> getGuardiansByStudent(Long studentId){

        var student = getStudent(studentId);

        return studentGuardianRepository.findAllByStudent(student).stream()
                .map(studentDtoService::studentGuardianToDto)
                .toList();
    }

    public GuardianResponseDto getGuardianById(Long id){
        var existingGuardian = guardianRepository.findById(id);
        Validate.isPresent(this, existingGuardian, GUARDIAN_NOT_FOUND, id);
        var guardian = existingGuardian.get();

        return studentDtoService.guardianToDto(guardian);
    }

    private TStudent getStudent(Long id){
        var existingStudent = studentRepository.findByDeletedAndId(Boolean.FALSE, id);
        Validate.isPresent(this, existingStudent, STUDENT_NOT_FOUND, id);
        return existingStudent.get();
    }

    private TGuardian getGuardian(Long id){
        var existingGuardian = guardianRepository.findByDeletedAndId(Boolean.FALSE, id);
        Validate.isPresent(this, existingGuardian, GUARDIAN_NOT_FOUND, id);
        return existingGuardian.get();
    }

    private TSchool getSchool(Long id){
        var existingSchool = schoolRepository.findById(id);
        Validate.isPresent(this, existingSchool, SCHOOL_NOT_FOUND, id);
        return existingSchool.get();
    }

    private List<TStudent> validateList0fStudentRequests(List<StudentRequestDto> studentsRequestDto){

        Validate.isTrue(this, !studentsRequestDto.isEmpty(), ExceptionType.BAD_REQUEST, NULL_STUDENTS);

        //validate schoolId
        var schoolIds = studentsRequestDto.stream().map(StudentRequestDto::schoolId).collect(Collectors.toSet());
        Validate.isTrue(this, schoolIds.size() == 1, ExceptionType.BAD_REQUEST, NOT_SAME_SCHOOL_ID);

        var school = getSchool(schoolIds.stream().findFirst().get());

        var existingEditingUser = userRepository.findById(auditService.getLoggedInUser().getId());
        final var editingUser = existingEditingUser.get();

        if(!editingUser.getUserType().equals(UserTypeEnum.ADMIN))
            Validate.isTrue(this, schoolUserRepository.existsBySchoolAndUser(school, editingUser), ExceptionType.ACCESS_DENIED, CANT_ASSIGN_SCHOOL, school.getId());


        //validate each student in list
        for (StudentRequestDto student : studentsRequestDto) {
            student.validate();
            Validate.isTrue(this, !studentRepository.existsBySchoolIdNumberAndSchool(student.schoolIdNumber(), school), ExceptionType.BAD_REQUEST, STUDENT_ID_ALREADY_TAKEN, student.schoolIdNumber());

            if (!student.nationalIdNumber().isEmpty())
                Validate.isTrue(this, !studentRepository.existsByNationalIdNumber(student.nationalIdNumber()), ExceptionType.BAD_REQUEST, NATIONAL_ID_ALREADY_TAKEN, student.nationalIdNumber());

            if (!student.email().isEmpty())
                Validate.isTrue(this, !studentRepository.existsByEmail(student.email()), ExceptionType.BAD_REQUEST, EMAIL_ALREADY_TAKEN, student.email());

            if (!student.email().isEmpty())
                Validate.isTrue(this, !studentRepository.existsByNationalIdNumber(student.email()), ExceptionType.BAD_REQUEST, NATIONAL_ID_ALREADY_TAKEN, student.email());

        }

        //check duplicate emails
        var allEmails = studentsRequestDto.stream().map(StudentRequestDto::email).toList();
        var duplicateEmails = allEmails.stream().filter(email -> Collections.frequency(allEmails, email) > 1).collect(Collectors.joining(", "));

        Validate.isTrue(this, duplicateEmails.isEmpty(), ExceptionType.BAD_REQUEST, EMAIL_DUPLICATES, duplicateEmails);

        var students = studentsRequestDto.stream()
                .map(this::getAuditedStudent
                ).toList();

        return studentRepository.saveAll(students);
    }

    private TStudent getAuditedStudent(StudentRequestDto studentRequestDto){
        var student = studentDtoService.dtoToTStudent(studentRequestDto);
        auditService.stampAuditedEntity(student);
        return student;
    }

    private String getNonExistingStudentUsername(){
        var studentUsername = "";
        do {
            studentUsername = StringUtil.getRandom6DigitString();
        }while(studentRepository.existsByStudentUsername(studentUsername));

        return studentUsername;
    }

    public PageResponseDto getAllStudents(Pageable pageable){

        var studentPage = studentRepository.findAll(pageable);
        var students = studentRepository.findAll(pageable).stream()
                .map(studentDtoService::studentToDto)
                .toList();

        return pageToDto(studentPage, students);
    }
}
