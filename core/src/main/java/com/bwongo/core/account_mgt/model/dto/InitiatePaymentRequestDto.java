package com.bwongo.core.account_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import java.math.BigDecimal;

import static com.bwongo.core.account_mgt.utils.AccountMsgConstants.*;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.INVALID_PHONE_NUMBER;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/13/23
 **/
public record InitiatePaymentRequestDto(
        String phoneNumber,
        BigDecimal amount,
        String depositorName
) {
    public void validate(){
        Validate.notEmpty(this, phoneNumber, NULL_PHONE_NUMBER);
        StringRegExUtil.stringOfInternationalPhoneNumber(this, phoneNumber, INVALID_PHONE_NUMBER);
        Validate.notNull(this, amount, ExceptionType.BAD_REQUEST, NULL_AMOUNT);
        Validate.isTrue(this, amount.compareTo(BigDecimal.ZERO) > 0, ExceptionType.BAD_REQUEST, INVALID_AMOUNT);
        Validate.notEmpty(this, depositorName, NULL_DEPOSITOR_NAME);
    }
}
