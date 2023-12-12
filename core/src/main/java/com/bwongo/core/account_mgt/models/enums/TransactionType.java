package com.bwongo.core.account_mgt.models.enums;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
public enum TransactionType {
    MOMO_DEPOSIT("depositing money from MOMO to account"),
    MOMO_WITHDRAW("withdrawing money from wallet to MOMO"),
    ACCOUNT_DEBIT("money leaving account"),
    ACCOUNT_CREDIT("money coming to account"),
    REFUND("refunding money from system account to MOMO");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
