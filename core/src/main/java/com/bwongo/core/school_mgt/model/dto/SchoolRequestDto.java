package com.bwongo.core.school_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.utils.EnumValidations;

import java.math.BigDecimal;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.NULL_PHYSICAL_ADDRESS;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
public record SchoolRequestDto(
        String schoolName,
        String username,
        String email,
        Long districtId,
        Long countryId,
        String phoneNumber,
        BigDecimal smsCost,
        String schoolCategory,
        double latitudeCoordinate,
        double longitudeCoordinate,
        String physicalAddress
) {
    public void validate(){
        Validate.notEmpty(schoolName, NULL_SCHOOL_NAME);
        Validate.notEmpty(username, NULL_USERNAME);
        Validate.notEmpty(email, NULL_EMAIL);
        StringRegExUtil.stringOfEmail(email, INVALID_EMAIL, email);
        Validate.notNull(districtId, ExceptionType.BAD_REQUEST, NULL_DISTRICT_ID);
        Validate.notNull(countryId, ExceptionType.BAD_REQUEST, NULL_COUNTRY_ID);
        Validate.notEmpty(phoneNumber, NULL_PHONE_NUMBER);
        Validate.notNull(smsCost, ExceptionType.BAD_REQUEST, NULL_SMS_COST);
        Validate.isTrue(smsCost.compareTo(BigDecimal.ZERO) > 0, ExceptionType.BAD_REQUEST, INVALID_AMOUNT);
        Validate.notEmpty(schoolCategory, NULL_SCHOOL_CATEGORY);
        Validate.isTrue(EnumValidations.isSchoolCategory(schoolCategory), ExceptionType.BAD_REQUEST, INVALID_SCHOOL_CATEGORY);
        Validate.notNull(latitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
        Validate.notNull(longitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
        Validate.notEmpty(physicalAddress, NULL_PHYSICAL_ADDRESS);
    }
}
