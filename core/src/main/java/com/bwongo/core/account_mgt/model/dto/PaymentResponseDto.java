package com.bwongo.core.account_mgt.model.dto;

import com.bwongo.core.account_mgt.model.enums.TransactionStatus;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/13/23
 **/
public record PaymentResponseDto(
        TransactionStatus transactionStatus,
        String externalTransactionReference
) {
}
