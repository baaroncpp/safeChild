package com.bwongo.core.account_mgt.model.dto;

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
