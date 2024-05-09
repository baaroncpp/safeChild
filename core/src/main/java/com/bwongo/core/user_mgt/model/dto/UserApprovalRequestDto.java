package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.base.utils.EnumValidations.isApprovalStatus;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
public record UserApprovalRequestDto(
        Long approvalId,
        //Long userId,
        String status
) {
    public void validate(){
        Validate.notNull(this, approvalId, ExceptionType.BAD_REQUEST, APPROVAL_ID_REQUIRED);
        Validate.notEmpty(this, status, APPROVAL_STATUS_REQUIRED);
        Validate.isTrue(this, isApprovalStatus(status), ExceptionType.BAD_REQUEST, INVALID_APPROVAL_STATUS);
    }
}
