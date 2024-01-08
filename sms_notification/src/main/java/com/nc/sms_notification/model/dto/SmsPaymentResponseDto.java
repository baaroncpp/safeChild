package com.nc.sms_notification.model.dto;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/19/23
 **/
public record SmsPaymentResponseDto(
        String transactionReference,
        String coreBankingStatus
) {
}
