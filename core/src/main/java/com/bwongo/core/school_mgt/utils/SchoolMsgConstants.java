package com.bwongo.core.school_mgt.utils;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
public class SchoolMsgConstants {

    private SchoolMsgConstants() { }

    public static final String INVALID_EMAIL = "%s invalid email address";
    public static final String NULL_SCHOOL_ID = "schoolId is null or empty";
    public static final String NULL_SCHOOL_NAME = "schoolName is null or empty";
    public static final String NULL_USERNAME = "username is null or empty";
    public static final String NULL_EMAIL = "email is null or empty";
    public static final String NULL_DISTRICT_ID = "districtId is null or empty";
    public static final String NULL_COORDINATE = "location coordinate is null or empty";
    public static final String NULL_COUNTRY_ID = "countryId is null or empty";
    public static final String NULL_PHONE_NUMBER = "phoneNumber is null or empty";
    public static final String NULL_SMS_COST = "smsCost is null or empty";
    public static final String NULL_SCHOOL_CATEGORY = "schoolCategory is null or empty";
    public static final String INVALID_AMOUNT = "amount must be greater than 0.0";
    public static final String INVALID_SCHOOL_CATEGORY = "invalid school Category, Options: DAY_CARE, PRIMARY, KINDERGARTEN, NURSERY, PRE_SCHOOL";
    public static final String SCHOOL_USERNAME_TAKEN = "School username: %s already taken";
    public static final String SCHOOL_PHONE_TAKEN = "School phoneNumber: %s already taken";
    public static final String SCHOOL_EMAIL_TAKEN = "School email: %s already taken";
    public static final String SCHOOL_NOT_FOUND = "School with ID: %s not found";
}
