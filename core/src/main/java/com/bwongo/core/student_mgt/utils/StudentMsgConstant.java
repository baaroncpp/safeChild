package com.bwongo.core.student_mgt.utils;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
public class StudentMsgConstant {
    private StudentMsgConstant() {  }

    public static final String NULL_FULL_NAME = "fullName is null or empty";
    public static final String NULL_ADDRESS = "address is null or empty";
    public static final String NULL_RELATION = "relation is null or empty";
    public static final String INVALID_RELATION = "invalid relation, Options: FATHER, MOTHER, BROTHER, SISTER, UNCLE, AUNT, OTHERS";
    public static final String NULL_IS_NOTIFIED = "isNotified is null or empty";
    public static final String INVALID_IDENTIFICATION_TYPE = "invalid identificationType, Options: DRIVING_PERMIT, PASSPORT, NATIONAL_ID";
    public static final String NULL_STUDENT_ID = "studentId is null or empty";
    public static final String NULL_SCHOOL_CARD_ID = "schoolIdNumber is null or empty";
    public static final String NULL_FIRST_NAME = "firstName is null or empty";
    public static final String NULL_SECOND_NAME = "secondName is null or empty";
    public static final String NULL_STUDENT_CLASS = "studentClass is null or empty";
    public static final String NULL_CAN_BE_NOTIFIED = "canBeNotified is null or empty";
    public static final String NATIONAL_ID_ALREADY_TAKEN = "national ID: %s already taken";
    public static final String GUARDIAN_PHONE_TAKEN = "phoneNumber: %s already taken";
    public static final String STUDENT_ID_ALREADY_TAKEN = "student ID: %s already taken";
    public static final String EMAIL_ALREADY_TAKEN = "email: %s already taken";
    public static final String STUDENT_NOT_FOUND = "student with ID: %s not found";
    public static final String GUARDIAN_NOT_FOUND = "guardian with ID: %s not found";
    public static final String EMAIL_DUPLICATES = "%s emails are duplicates";
    public static final String NULL_STUDENTS = "student list is null or empty";
    public static final String NULL_PHYSICAL_ADDRESS = "physicalAddress is null or empty";
    public static final String NOT_SAME_SCHOOL_ID = "all students should have the same schoolId";
}
