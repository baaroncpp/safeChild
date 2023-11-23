package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/22/23
 **/
public record ChangePasswordRequestDto(
        String newPassword,
        String oldPassword
) {
    public void validate(){
        Validate.notEmpty(newPassword, NULL_NEW_PASSWORD);
        Validate.notEmpty(oldPassword, NULL_OLD_PASSWORD);
        StringRegExUtil.stringOfStandardPassword(newPassword, STANDARD_PASSWORD);
    }
}
