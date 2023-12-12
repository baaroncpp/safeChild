package com.bwongo.core.account_mgt.models.enums;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
public enum TransactionStatus {
    PENDING("Transaction has not been fully processed"),
    SUCCESSFUL("Transaction has been processed by all parties involved"),
    FAILED("Transaction processing has failed");

    private final String description;

    TransactionStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return  this.description;
    }
}
