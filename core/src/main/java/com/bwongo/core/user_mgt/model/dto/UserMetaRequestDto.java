package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.enums.GenderEnum;

import java.util.Date;

import static com.bwongo.core.base.utils.EnumValidations.isIdentificationType;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record UserMetaRequestDto(
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
         String identificationNumber
         //String pin
) {
    public void validate(){
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
        //Validate.notEmpty(this, pin, NULL_PIN);
        //StringRegExUtil.stringOfOnlyNumbers(this, pin, String.format(INVALID_PIN, pin));
        //Validate.isTrue(this, pin.length() == 4, ExceptionType.BAD_REQUEST, PIN_BAD_LENGTH);

        if(!phoneNumber2.isEmpty()){
            StringRegExUtil.stringOfInternationalPhoneNumber(this, phoneNumber, INVALID_SECOND_PHONE_NUMBER);
        }
    }
}
