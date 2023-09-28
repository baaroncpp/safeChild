package com.bwongo.core.student_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.repository.SchoolRepository;
import com.bwongo.core.student_mgt.model.dto.*;
import com.bwongo.core.student_mgt.model.jpa.TGuardian;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.model.jpa.TStudentGuardian;
import com.bwongo.core.student_mgt.repository.GuardianRepository;
import com.bwongo.core.student_mgt.repository.StudentGuardianRepository;
import com.bwongo.core.student_mgt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.SCHOOL_NOT_FOUND;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
@Service
@RequiredArgsConstructor
public class StudentService {

    private final SchoolRepository schoolRepository;
    private final GuardianRepository guardianRepository;
    private final StudentRepository studentRepository;
    private final StudentGuardianRepository studentGuardianRepository;
    private final StudentDtoService studentDtoService;
    private final AuditService auditService;

    public StudentResponseDto addStudent(StudentRequestDto studentRequestDto){

        studentRequestDto.validate();
        final var nationalIdNumber = studentRequestDto.nationalIdNumber();
        final var schoolIdNumber = studentRequestDto.schoolIdNumber();
        final var schoolId = studentRequestDto.schoolId();

        var school = getSchool(schoolId);

        if(!nationalIdNumber.isEmpty())
            Validate.isTrue(!studentRepository.existsByNationalIdNumber(nationalIdNumber), ExceptionType.BAD_REQUEST, NATIONAL_ID_ALREADY_TAKEN, nationalIdNumber);

        Validate.isTrue(!studentRepository.existsBySchoolIdNumberAndSchool(schoolIdNumber, school), ExceptionType.BAD_REQUEST, STUDENT_ID_ALREADY_TAKEN, schoolIdNumber);

        var student = studentDtoService.dtoToTStudent(studentRequestDto);
        auditService.stampAuditedEntity(student);

        return studentDtoService.studentToDto(studentRepository.save(student));
    }

    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto){

        studentRequestDto.validate();

        var existingStudent = getStudent(id);
        final var nationalIdNumber = studentRequestDto.nationalIdNumber();
        final var schoolIdNumber = studentRequestDto.schoolIdNumber();
        final var schoolId = studentRequestDto.schoolId();

        var school = getSchool(schoolId);

        if(!nationalIdNumber.isEmpty() && !nationalIdNumber.equals(existingStudent.getNationalIdNumber()))
            Validate.isTrue(studentRepository.existsByNationalIdNumber(nationalIdNumber), ExceptionType.BAD_REQUEST, NATIONAL_ID_ALREADY_TAKEN, nationalIdNumber);

        if(!schoolIdNumber.equals(existingStudent.getSchoolIdNumber()))
            Validate.isTrue(studentRepository.existsBySchoolIdNumberAndSchool(schoolIdNumber, school), ExceptionType.BAD_REQUEST, STUDENT_ID_ALREADY_TAKEN, schoolIdNumber);

        var updatedStudent = studentDtoService.dtoToTStudent(studentRequestDto);
        auditService.stampAuditedEntity(updatedStudent);
        updatedStudent.setId(existingStudent.getId());

        return studentDtoService.studentToDto(studentRepository.save(updatedStudent));
    }

    public StudentResponseDto getStudentById(Long id){
        return studentDtoService.studentToDto(getStudent(id));
    }

    public StudentResponseDto getStudentByStudentIdNumberAndSchoolId(String studentIdNumber, Long schoolId){

        final var school = getSchool(schoolId);

        var existingStudent = studentRepository.findByDeletedAndSchoolIdNumberAndSchool(Boolean.FALSE, studentIdNumber, school);
        Validate.isPresent(existingStudent, STUDENT_NOT_FOUND, studentIdNumber);
        final var student = existingStudent.get();

        return studentDtoService.studentToDto(student);
    }

    public List<StudentResponseDto> getAllStudents(Pageable pageable, Long schoolId){

        var school = getSchool(schoolId);

        return studentRepository.findAllByDeletedAndSchool(pageable, Boolean.FALSE, school).stream()
                .map(studentDtoService::studentToDto)
                .collect(Collectors.toList());
    }

    public StudentGuardianDto addStudentGuardian(GuardianRequestDto guardianRequestDto){

        guardianRequestDto.validate();
        final var phoneNumber = guardianRequestDto.phoneNumber();
        final var studentId = guardianRequestDto.studentId();

        var existingStudent = getStudent(studentId);

        Validate.isTrue(!guardianRepository.existsByPhoneNumberAndDeleted(phoneNumber, Boolean.FALSE),
                ExceptionType.BAD_REQUEST,
                GUARDIAN_PHONE_TAKEN,
                phoneNumber);

        var guardian = studentDtoService.dtoToTGuardian(guardianRequestDto);
        auditService.stampAuditedEntity(guardian);

        var savedGuardian = guardianRepository.save(guardian);

        var studentGuardian = new TStudentGuardian();
        studentGuardian.setGuardian(savedGuardian);
        studentGuardian.setStudent(existingStudent);
        auditService.stampAuditedEntity(studentGuardian);

        return studentDtoService.studentGuardianToDto(studentGuardianRepository.save(studentGuardian));
    }

    public StudentGuardianDto assignGuardianToStudent(Long studentId, Long guardianId){

        var existingStudent = getStudent(studentId);
        var existingGuardian = getGuardian(guardianId);

        var studentGuardian = new TStudentGuardian();
        studentGuardian.setGuardian(existingGuardian);
        studentGuardian.setStudent(existingStudent);
        auditService.stampAuditedEntity(studentGuardian);

        return studentDtoService.studentGuardianToDto(studentGuardianRepository.save(studentGuardian));
    }

    public List<StudentGuardianDto> getStudentsByGuardian(Long guardianId){

        var guardian = getGuardian(guardianId);

        return studentGuardianRepository.findAllByGuardian(guardian).stream()
                .map(studentDtoService::studentGuardianToDto)
                .collect(Collectors.toList());
    }

    public List<StudentGuardianDto> getGuardiansByStudent(Long studentId){

        var student = getStudent(studentId);

        return studentGuardianRepository.findAllByStudent(student).stream()
                .map(studentDtoService::studentGuardianToDto)
                .collect(Collectors.toList());
    }

    private TStudent getStudent(Long id){
        var existingStudent = studentRepository.findByDeletedAndId(Boolean.FALSE, id);
        Validate.isPresent(existingStudent, STUDENT_NOT_FOUND, id);
        return existingStudent.get();
    }

    private TGuardian getGuardian(Long id){
        var existingGuardian = guardianRepository.findByDeletedAndId(Boolean.FALSE, id);
        Validate.isPresent(existingGuardian, GUARDIAN_NOT_FOUND, id);
        return existingGuardian.get();
    }

    private TSchool getSchool(Long id){
        var existingSchool = schoolRepository.findById(id);
        Validate.isPresent(existingSchool, SCHOOL_NOT_FOUND, id);
        return existingSchool.get();
    }
}
