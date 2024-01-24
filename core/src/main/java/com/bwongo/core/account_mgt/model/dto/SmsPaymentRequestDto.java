package com.bwongo.core.account_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import java.math.BigDecimal;

import static com.bwongo.core.account_mgt.utils.AccountMsgConstants.*;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.INVALID_AMOUNT;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/18/23
 **/
public record SmsPaymentRequestDto(
        Long notificationId
) {
    public void validate(){
        Validate.notNull(this, notificationId, ExceptionType.BAD_REQUEST, NULL_NOTIFICATION_ID);
    }
}
