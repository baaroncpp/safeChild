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
    public static final String NULL_NOTIFICATION_ID = "notificationId is null or empty";
    public static final String NOTIFICATION_NOT_FOUND = "notification with ID: %s not found";
    public static final String ACCOUNT_NUMBER_NOT_FOUND = "account with number: %s not found";
    public static final String LOW_ACCOUNT_BALANCE = "account: %s, balance is too low";
    public static final String PAYMENT_FAILED_AT_CORE_BANKING = "%s , payment at core banking";
    public static final String INVALID_ACCOUNT_TYPE = "%s is invalid accountType: Options are SYSTEM, SCHOOL";

}
