package com.nc.safechild.utils;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/14/23
 **/
public class MessageConstants {

    private MessageConstants() { }

    public static final String NULL_FROM_ACCOUNT = "fromAccount is null or empty";
    public static final String NULL_TO_ACCOUNT = "toAccount is null or empty";
    public static final String NULL_TO_DESCRIPTION = "description is null or empty";
    public static final String NULL_VENDOR = "vendor is null or empty";
    public static final String NULL_FLOAT_INITIATOR = "floatInitiator is null or empty";
    public static final String NULL_FLOAT_APPROVER = "floatApprover is null or empty";
    public static final String NULL_AIRLINE = "airline is null or empty";
    public static final String INVALID_AMOUNT = "amount must be greater than 0.0";
    public static final String NULL_USERNAME = "username is null or empty";
    public static final String NULL_PIN = "pin is null or empty";
    public static final String USER_BLOCKED = "user %s is blocked";
    public static final String INVALID_CREDENTIALS = "invalid user parameters";
    public static final String STUDENT_NOT_FOUND = "student %s not found";
    public static final String NULL_STUDENT_STATUS = "studentStatus is null or empty";
    public static final String INVALID_STUDENT_STATUS = "%s is invalid studentStatus: VALID OPTIONS: PICK_UP, DROP_OFF, ON_SCHOOL, OOF_SCHOOL, IN_CLASS";
    public static final String SCHOOL_INSUFFICIENT_FUNDS = "Insufficient funds on school, please top up";
    public static final String MEMBER_PRINCIPLE_TYPE = "USER";
    public static final String STUDENT_SCHOOL = "std1_school";
    public static final String PERFORMED_BY = "performed_by";
    public static final String APP_REF = "app_ref";
    public static final String STUDENT_STATUS = "std1_status";
    public static final String NULL_PERFORMED_BY = "performedByUsername is null or empty";
    public static final String NULL_APP_REF = "appRef is null or empty";
    public static final String NO_EVENTS = "no daily events";
}
