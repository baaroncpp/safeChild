package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record RoleRequestDto(
        String name,
        String note
) {
    public void validate(){
        Validate.notEmpty(this, name, ROLE_NAME_REQUIRED);
        Validate.notEmpty(this, note, ROLE_DESCRIPTION_REQUIRED);
    }
}
