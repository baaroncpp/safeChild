package com.bwongo.core.student_mgt.service;

import com.bwongo.core.base.model.enums.IdentificationType;
import com.bwongo.core.base.model.enums.Relation;
import com.bwongo.core.base.service.BaseDtoService;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.student_mgt.model.dto.*;
import com.bwongo.core.student_mgt.model.jpa.*;
import com.bwongo.core.trip_mgt.model.dto.TripResponseDto;
import com.bwongo.core.trip_mgt.service.TripDtoService;
import com.bwongo.core.user_mgt.service.UserDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
@Service
@RequiredArgsConstructor
public class StudentDtoService {

    private final UserDtoService userDtoService;
    private final SchoolDtoService schoolDtoService;
    private final BaseDtoService baseDtoService;

    public TStudent dtoToTStudent(StudentRequestDto studentRequestDto){

        if(studentRequestDto == null){
            return null;
        }

        var school = new TSchool();
        school.setId(studentRequestDto.schoolId());

        var student = new TStudent();
        student.setFirstName(studentRequestDto.firstName());
        student.setSecondName(studentRequestDto.secondName());
        student.setSchoolIdNumber(studentRequestDto.schoolIdNumber());
        student.setEmail(studentRequestDto.email());
        student.setStudentClass(studentRequestDto.studentClass());
        student.setSchool(school);
        student.setCanBeNotified(studentRequestDto.canBeNotified());
        student.setNationalIdNumber(studentRequestDto.nationalIdNumber());
        student.setPhysicalAddress(studentRequestDto.physicalAddress());
        student.setQrCode(studentRequestDto.qrCodeString());

        return student;
    }

    public StudentResponseDto studentToDto(TStudent student){

        if(student == null){
            return null;
        }

        return new StudentResponseDto(
                student.getId(),
                student.getCreatedOn(),
                student.getModifiedOn(),
                userDtoService.tUserToDto(student.getCreatedBy()),
                userDtoService.tUserToDto(student.getModifiedBy()),
                student.getFirstName(),
                student.getSecondName(),
                student.getSchoolIdNumber(),
                student.getNationalIdNumber(),
                student.getEmail(),
                student.getStudentClass(),
                schoolDtoService.schoolToDto(student.getSchool()),
                student.getProfileImagePathUrl(),
                student.getIdImagePathUrl(),
                student.isCanBeNotified(),
                student.getPhysicalAddress(),
                student.getStudentUsername(),
                student.getQrCode()
        );
    }

    public TGuardian dtoToTGuardian(GuardianRequestDto guardianRequestDto){

        if(guardianRequestDto == null){
            return null;
        }

        var guardian = new TGuardian();
        guardian.setFullName(guardianRequestDto.fullName());
        guardian.setPhoneNumber(guardianRequestDto.phoneNumber());
        guardian.setAddress(guardianRequestDto.address());
        guardian.setIdentificationType(IdentificationType.valueOf(guardianRequestDto.identificationType()));
        guardian.setIdNumber(guardianRequestDto.idNumber());
        guardian.setNotified(guardianRequestDto.isNotified());

        if(!guardianRequestDto.relation().isEmpty())
            guardian.setRelation(Relation.valueOf(guardianRequestDto.relation()));

        return guardian;
    }

    public GuardianResponseDto guardianToDto(TGuardian guardian){

        if(guardian == null){
            return null;
        }

        return new GuardianResponseDto(
            guardian.getId(),
            guardian.getCreatedOn(),
            guardian.getModifiedOn(),
            userDtoService.tUserToDto(guardian.getCreatedBy()),
            userDtoService.tUserToDto(guardian.getModifiedBy()),
            guardian.getFullName(),
            guardian.getPhoneNumber(),
            guardian.getAddress(),
            guardian.getRelation(),
            guardian.getIdentificationType(),
            guardian.getIdNumber(),
            guardian.isNotified()
        );
    }

    public StudentGuardianDto studentGuardianToDto(TStudentGuardian studentGuardian){

        if(studentGuardian == null){
            return null;
        }

        return new StudentGuardianDto(
                studentGuardian.getId(),
                studentGuardian.getCreatedOn(),
                studentGuardian.getModifiedOn(),
                userDtoService.tUserToDto(studentGuardian.getCreatedBy()),
                userDtoService.tUserToDto(studentGuardian.getModifiedBy()),
                studentToDto(studentGuardian.getStudent()),
                guardianToDto(studentGuardian.getGuardian())
        );

    }

    public StudentDayResponseDto studentDayToDto(StudentDay studentDay){

        if(studentDay == null){
            return null;
        }

        return new StudentDayResponseDto(
                studentDay.getId(),
                studentDay.getCreatedOn(),
                studentDay.getModifiedOn(),
                userDtoService.tUserToDto(studentDay.getCreatedBy()),
                userDtoService.tUserToDto(studentDay.getModifiedBy()),
                studentDay.getStudentStatus(),
                userDtoService.tUserToDto(studentDay.getStaff()),
                studentToDto(studentDay.getStudent()),
                schoolDtoService.schoolToDto(studentDay.getSchool()),
                studentDay.getSchoolDate(),
                studentDay.isOnTrip(),
                baseDtoService.locationToDto(studentDay.getLocation())
        );
    }
}
