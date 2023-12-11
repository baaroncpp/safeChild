package com.bwongo.core.notify_mgt.utils;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/11/23
 **/
public class NotificationMsgConstants {
    private NotificationMsgConstants() { }

    public static final String NULL_APP_REF = "null or empty appRef";
    public static final String STUDENT_HAS_ALREADY_BEEN_IN_STATUS = "%s has already been %s";
    public static final String STUDENT_ALREADY_SIGNED_IN = "student %s already SIGNED IN to school";
    public static final String STUDENT_STATUS_NOT_ALLOWED = "StudentStatus %s not allowed";
    public static final String STUDENT_NOT_CHECKED_IN = "Student %s was not signed in, cannot be signed out";
    public static final String STUDENT_STAFF_NOT_SAME_SCHOOL = "User %s and student %s are not from the same school";
    public static final String STUDENT_ALREADY_DROPPED_OFF = "Student %s already dropped off";
    public static final String INVALID_STUDENT_STATUS_FOR_TRIP = "studentStatus %s does not correspond to tripType";
    public static final String STUDENT_NOT_PICKED_UP = "student : %s not picked up on this trip";
    public static final String STUDENT_WAS_NOT_SIGNED_OUT = "Student %s was not signed out";
    public static final String STUDENT_NOT_ON_TRIP = "Student %s not on the trip";
    public static final String BULK_ONLY_PICK_UP = "Can only do BULK DROP OFF on PICK_UP trip";

}
