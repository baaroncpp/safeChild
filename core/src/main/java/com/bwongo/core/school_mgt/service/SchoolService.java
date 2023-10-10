package com.bwongo.core.school_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.repository.TLocationRepository;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.core_banking.service.MemberService;
import com.bwongo.core.school_mgt.model.dto.SchoolRequestDto;
import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.school_mgt.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final SchoolDtoService schoolDtoService;
    private final AuditService auditService;
    private final TLocationRepository locationRepository;
    private final MemberService memberService;

    @Transactional
    public SchoolResponseDto addSchool(SchoolRequestDto schoolRequestDto){

        schoolRequestDto.validate();

        var email = schoolRequestDto.email();
        var phone = schoolRequestDto.phoneNumber();
        var username = schoolRequestDto.username();

        Validate.isTrue(!schoolRepository.existsByUsername(username), ExceptionType.BAD_REQUEST, SCHOOL_USERNAME_TAKEN, username);
        Validate.isTrue(!schoolRepository.existsByPhoneNumber(phone), ExceptionType.BAD_REQUEST, SCHOOL_PHONE_TAKEN, phone);
        Validate.isTrue(!schoolRepository.existsByEmail(email), ExceptionType.BAD_REQUEST, SCHOOL_EMAIL_TAKEN, email);

        var school = schoolDtoService.dtoToTSchool(schoolRequestDto);
        var location = school.getLocation();

        auditService.stampLongEntity(location);
        auditService.stampAuditedEntity(school);

        var savedLocation = locationRepository.save(location);
        school.setLocation(savedLocation);
        school.setAccountNumber(getNonExistingSchoolAccountNumber());

        System.out.println(school.getAccountNumber());

        var coreBankingId = memberService.addSchoolToCoreBanking(school);
        school.setCoreBankingId(coreBankingId);

        var result = schoolRepository.save(school);

        return schoolDtoService.schoolToDto(result);
    }

    @Transactional
    public SchoolResponseDto updateSchool(Long id, SchoolRequestDto schoolRequestDto){

        schoolRequestDto.validate();

        var existingSchool = schoolRepository.findById(id);
        Validate.isPresent(existingSchool, SCHOOL_NOT_FOUND, id);
        final var school = existingSchool.get();

        var email = schoolRequestDto.email();
        var phone = schoolRequestDto.phoneNumber();
        var username = schoolRequestDto.username();

        Validate.isTrue(!school.isDeleted(), ExceptionType.BAD_REQUEST, SCHOOL_NOT_FOUND, id);

        if(!username.equals(school.getUsername()))
            Validate.isTrue(!schoolRepository.existsByUsername(username), ExceptionType.BAD_REQUEST, SCHOOL_USERNAME_TAKEN, username);

        if(!email.equals(school.getEmail()))
            Validate.isTrue(!schoolRepository.existsByEmail(email), ExceptionType.BAD_REQUEST, SCHOOL_EMAIL_TAKEN, email);

        if(!phone.equals(school.getPhoneNumber()))
            Validate.isTrue(!schoolRepository.existsByPhoneNumber(phone), ExceptionType.BAD_REQUEST, SCHOOL_PHONE_TAKEN, phone);

        var updatedSchool = schoolDtoService.dtoToTSchool(schoolRequestDto);
        var updatedLocation = updatedSchool.getLocation();

        var currentLocation = school.getLocation();
        currentLocation.setLongitude(updatedLocation.getLongitude());
        currentLocation.setLatitude(updatedLocation.getLatitude());
        auditService.stampLongEntity(currentLocation);

        var savedUpdatedLocation = locationRepository.save(currentLocation);

        updatedSchool.setId(id);
        updatedSchool.setLocation(savedUpdatedLocation);

        auditService.stampAuditedEntity(updatedSchool);

        var savedUpdatedSchool = schoolRepository.save(updatedSchool);
        //memberService.updateSchoolToCoreBanking(savedUpdatedSchool.getCoreBankingId(), savedUpdatedSchool);

        return schoolDtoService.schoolToDto(savedUpdatedSchool);
    }

    public SchoolResponseDto getSchoolById(Long id){

        var existingSchool = schoolRepository.findById(id);
        Validate.isPresent(existingSchool, SCHOOL_NOT_FOUND, id);
        final var school = existingSchool.get();

        Validate.isTrue(!school.isDeleted(), ExceptionType.BAD_REQUEST, SCHOOL_NOT_FOUND, id);

        return schoolDtoService.schoolToDto(school);
    }

    public List<SchoolResponseDto> getAllSchools(Pageable pageable){
        return schoolRepository.findAllByDeleted(Boolean.FALSE, pageable).stream()
                .map(schoolDtoService::schoolToDto)
                .collect(Collectors.toList());
    }

    private String getNonExistingSchoolAccountNumber(){
        var accountNumber = "";
        do {
            accountNumber = StringUtil.getRandom6DigitString();
        }while(schoolRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}
