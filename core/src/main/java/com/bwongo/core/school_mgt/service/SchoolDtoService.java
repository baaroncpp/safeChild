package com.bwongo.core.school_mgt.service;

import com.bwongo.core.base.model.enums.SchoolCategory;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.model.jpa.TDistrict;
import com.bwongo.core.base.model.jpa.TLocation;
import com.bwongo.core.base.service.BaseDtoService;
import com.bwongo.core.school_mgt.model.dto.SchoolRequestDto;
import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.user_mgt.model.dto.SchoolUserResponseDto;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.service.UserDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
@Service
@RequiredArgsConstructor
public class SchoolDtoService {

    private final BaseDtoService baseDtoService;
    private final UserDtoService userDtoService;

    public SchoolUserResponseDto tUserToUserSchoolDto(TUser user, TSchool school){

        if(user == null){
            return null;
        }

        return new SchoolUserResponseDto(
                user.getId(),
                user.getCreatedOn(),
                user.getModifiedOn(),
                user.getUsername(),
                user.isAccountLocked(),
                user.isAccountExpired(),
                user.isCredentialExpired(),
                user.isApproved(),
                userDtoService.tUserGroupToDto(user.getUserGroup()),
                user.getDeleted(),
                user.getApprovedBy(),
                user.getUserType(),
                user.getUserMetaId(),
                schoolToDto(school)
        );
    }

    public SchoolResponseDto schoolToDto(TSchool school){

        if(school == null){
            return null;
        }

        return new SchoolResponseDto(
                school.getId(),
                school.getCreatedOn(),
                school.getModifiedOn(),
                userDtoService.tUserToDto(school.getCreatedBy()),
                userDtoService.tUserToDto(school.getModifiedBy()),
                school.getSchoolName(),
                school.getGroupId(),
                school.getUsername(),
                school.getEmail(),
                baseDtoService.countryToDto(school.getCountry()),
                baseDtoService.districtToDto(school.getDistrict()),
                school.getPhoneNumber(),
                school.getSmsCost(),
                school.getReferenceSchoolId(),
                school.getSchoolCategory(),
                school.isAssigned(),
                baseDtoService.locationToDto(school.getLocation()),
                school.getPhysicalAddress()
        );
    }

    public TSchool dtoToTSchool(SchoolRequestDto schoolRequestDto){

        if(schoolRequestDto == null){
            return null;
        }

        var district = new TDistrict();
        district.setId(schoolRequestDto.districtId());

        var country = new TCountry();
        country.setId(schoolRequestDto.countryId());

        var location = new TLocation();
        location.setLatitude(schoolRequestDto.latitudeCoordinate());
        location.setLongitude(schoolRequestDto.longitudeCoordinate());

        var school = new TSchool();
        school.setSchoolName(schoolRequestDto.schoolName());
        //school.setUsername(schoolRequestDto.username());
        school.setEmail(schoolRequestDto.email());
        school.setDistrict(district);
        school.setCountry(country);
        school.setPhoneNumber(schoolRequestDto.phoneNumber());
        school.setSmsCost(schoolRequestDto.smsCost());
        school.setSchoolCategory(SchoolCategory.valueOf(schoolRequestDto.schoolCategory()));
        school.setLocation(location);
        school.setPhysicalAddress(schoolRequestDto.physicalAddress());

        return school;
    }
}
