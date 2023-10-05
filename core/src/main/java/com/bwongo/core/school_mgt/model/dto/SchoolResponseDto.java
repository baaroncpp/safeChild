package com.bwongo.core.school_mgt.model.dto;

import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.dto.DistrictResponseDto;
import com.bwongo.core.base.model.dto.LocationResponseDto;
import com.bwongo.core.base.model.enums.SchoolCategory;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.model.jpa.TDistrict;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
public record SchoolResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        UserResponseDto createdBy,
        UserResponseDto modifiedBy,
        String schoolName,
        Long groupId,
        String username,
        String email,
        CountryResponseDto country,
        DistrictResponseDto district,
        String phoneNumber,
        BigDecimal smsCost,
        String referenceSchoolId,
        SchoolCategory schoolCategory,
        boolean isAssigned,
        LocationResponseDto location,
        String address
) {
}
