package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.base.utils.EnumValidations.*;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_SCHOOL_ID;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
public record SchoolUserRequestDto (
        String username,
        String password,
        Long userGroupId,
        Long approvedBy,
        String userType,
        Long schoolId
) {
    public void validate(){
        Validate.notEmpty(username, USERNAME_REQUIRED);
        Validate.notEmpty(password, PASSWORD_REQUIRED);
        Validate.notNull(userGroupId, ExceptionType.BAD_REQUEST, USER_GROUP_ID_REQUIRED);
        Validate.notNull(userType, ExceptionType.BAD_REQUEST, USER_TYPE_REQUIRED);
        Validate.isTrue(isSchoolUserType(userType), ExceptionType.BAD_REQUEST, VALID_SCHOOL_USER_TYPE);
        StringRegExUtil.stringOfOnlyNumbersAndChars(username, USERNAME_SHOULD_CONTAIN_ONLY_CHARS_AND_NUMBERS);
        StringRegExUtil.stringOfStandardPassword(password, STANDARD_PASSWORD);
        Validate.notNull(schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);
    }
}


