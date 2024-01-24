package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.base.utils.EnumValidations.isUserType;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
public record UserRequestDto(
         String username,
         String password,
         Long userGroupId,
         Long approvedBy,
         String userType
) {
    public void validate(){
        Validate.notEmpty(this, username, USERNAME_REQUIRED);
        //Validate.notEmpty(password, PASSWORD_REQUIRED);
        Validate.notNull(this, userGroupId, ExceptionType.BAD_REQUEST, USER_GROUP_ID_REQUIRED);
        Validate.notNull(this, userType, ExceptionType.BAD_REQUEST, USER_TYPE_REQUIRED);
        Validate.isTrue(this, isUserType(userType), ExceptionType.BAD_REQUEST, VALID_USER_TYPE);
        StringRegExUtil.stringOfOnlyNumbersAndChars(this, username, USERNAME_SHOULD_CONTAIN_ONLY_CHARS_AND_NUMBERS);
        //StringRegExUtil.stringOfStandardPassword(password, STANDARD_PASSWORD);
    }
}
