package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import java.util.Date;

import static com.bwongo.core.base.utils.EnumValidations.*;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_SCHOOL_ID;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
public record SchoolUserRequestDto (
        String firstName,
        String lastName,
        String middleName,
        String phoneNumber,
        String phoneNumber2,
        String gender,
        Date birthDate,
        String email,
        Long countryId,
        String identificationType,
        String identificationNumber,
        String pin,
        String password,
        Long userGroupId,
        Long approvedBy,
        String userType,
        Long schoolId
) {
    public void validate(){
        //Validate.notEmpty(password, PASSWORD_REQUIRED);
        Validate.notNull(this, userGroupId, ExceptionType.BAD_REQUEST, USER_GROUP_ID_REQUIRED);
        Validate.notNull(this, userType, ExceptionType.BAD_REQUEST, USER_TYPE_REQUIRED);
        Validate.isTrue(this, isSchoolUserType(userType), ExceptionType.BAD_REQUEST, VALID_SCHOOL_USER_TYPE);
        //StringRegExUtil.stringOfStandardPassword(password, STANDARD_PASSWORD);
        Validate.notNull(this, schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);

        Validate.notEmpty(this, lastName, LAST_NAME_REQUIRED);
        Validate.notEmpty(this, firstName, FIRST_NAME_REQUIRED);
        StringRegExUtil.stringOfOnlyCharsNoneCaseSensitive(this, lastName, LAST_NAME_ONLY_CHARACTERS);
        StringRegExUtil.stringOfOnlyCharsNoneCaseSensitive(this, lastName, FIRST_NAME_ONLY_CHARACTERS);
        Validate.notEmpty(this, phoneNumber, PHONE_NUMBER_REQUIRED);
        StringRegExUtil.stringOfInternationalPhoneNumber(this, phoneNumber, INVALID_PHONE_NUMBER);
        Validate.notEmpty(this, email, EMAIL_REQUIRED);
        StringRegExUtil.stringOfEmail(this, email, INVALID_EMAIL);
        Validate.notEmpty(this, identificationNumber, IDENTIFICATION_NUMBER_REQUIRED);
        Validate.notNull(this, countryId, ExceptionType.BAD_REQUEST, COUNTRY_ID_REQUIRED);
        Validate.notNull(this, birthDate, ExceptionType.BAD_REQUEST, DOB_REQUIRED);
        Validate.notNull(this, gender, ExceptionType.BAD_REQUEST, GENDER_REQUIRED);
        Validate.notNull(this, identificationType, ExceptionType.BAD_REQUEST, IDENTIFICATION_TYPE_REQUIRED);
        Validate.isTrue(this, isIdentificationType(identificationType), ExceptionType.BAD_REQUEST, INVALID_IDENTIFICATION_TYPE);
        //Validate.notEmpty(pin, NULL_PIN);
        //StringRegExUtil.stringOfOnlyNumbers(pin, String.format(INVALID_PIN, pin));
        //Validate.isTrue(pin.length() == 4, ExceptionType.BAD_REQUEST, PIN_BAD_LENGTH);

        if(!phoneNumber2.isEmpty()){
            StringRegExUtil.stringOfInternationalPhoneNumber(this, phoneNumber, INVALID_SECOND_PHONE_NUMBER);
        }
    }
}


