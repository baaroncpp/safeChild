package com.bwongo.core.account_mgt.utils;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/13/23
 **/
public class AccountMsgConstants {

    private AccountMsgConstants() {  }

    public static final String NULL_AMOUNT = "amount is null or empty";
    public static final String NULL_DEPOSITOR_NAME = "depositorName is null or empty";
    public static final String DEPOSIT_NOT_FOUND = "MOMO deposit with transactionReference: %s not found";
    public static final String NULL_NETWORK = "network is null or empty";
    public static final String INVALID_NETWORK = "invalid network";
    public static final String SCHOOL_HAS_NO_ACCOUNT = "School has no account, Please make your initial deposit";

}
