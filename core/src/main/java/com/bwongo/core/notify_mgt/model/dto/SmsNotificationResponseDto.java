package com.bwongo.core.notify_mgt.model.dto;

import com.bwongo.core.base.model.enums.SmsStatus;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/2/24
 * @Time 9:25 AM
 **/
public record SmsNotificationResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        String receiver,
        String sender,
        String message,
        SmsStatus status,
        String statusNote,
        String transactionId,
        String externalTransactionId,
        String accountNumber
) {
}
