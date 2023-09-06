package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record GroupAuthorityRequestDto(
        Long userGroupId,
        Long permissionId
) {
    public void validate(){
        Validate.notNull(userGroupId, ExceptionType.BAD_REQUEST, USER_GROUP_ID_REQUIRED);
        Validate.notNull(permissionId, ExceptionType.BAD_REQUEST, PERMISSION_ID_REQUIRED);
    }
}
