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
    public static final String NULL_SCHOOL_ID = "schoolId is null or empty";
    public static final String NULL_TRIP_TYPE = "tripType is null or empty";
    public static final String INVALID_TRIP_TYPE = "Invalid tripType, options: PICKUP, DROP_OFF";
    public static final String EXISTING_IN_PROGRESS_TRIP = "cannot create a new trip, IN_PROGRESS trip exits";
    public static final String EXISTING_OPEN_TRIP = "cannot create a new trip, OPEN trip exits";
    public static final String TRIP_NOT_FOUND = "trip with ID: %s not found";
    public static final String TRIP_SAME_STATUS = "trip already in %s status";
    public static final String NULL_TRIP_ID = "tripId is null or empty";
    public static final String INVALID_STUDENT_STATUS_FOR_TRIP = "studentStatus %s does not correspond to tripType";
    public static final String STUDENT_ALREADY_ON_TRIP = "Student is already on the trip";
    public static final String STUDENT_NOT_ON_TRIP = "Student not on the trip";
    public static final String ALREADY_ON_SCHOOL = "Student %s is already signed in to school";
    public static final String STUDENT_NOT_SIGNED_IN = "Student %s was not signed in";
    public static final String ALREADY_SIGNED_OUT = "Student %s already signed out";
    public static final String TRIP_ENDED = "trip is already ended";
}
