package com.bwongo.core.account_mgt.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Setter
@Getter
public class CollectionDataDto {
    private String transactionSystemId;
    private String transactionReference;
    private String transactionStatus;
    private BigDecimal amount;
    private String msisdn;
    private String channel;
    private boolean customerCharged;
    private String currencyCode;
    private String currencyName;
    private String MNOTransactionReferenceId;
    private String transactionExternalReference;
    private String transactionExternalNarrative;
    private String transactionInitiationDate;
    private String transactionCompletionDate;
    private String transactionEntryGeneralType;
    private String transactionEntryDesignation;
    private String transactionEntrySpecificType;
}
