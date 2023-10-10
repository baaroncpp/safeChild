package com.bwongo.core.user_mgt.utils;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public class UserMsgConstants {

    private UserMsgConstants() { }

    public static final String USER_GROUP_ID_REQUIRED = "User group ID is required";
    public static final String PERMISSION_ID_REQUIRED = "User group ID is required";
    public static final String ROLE_NAME_REQUIRED = "Role name is required";
    public static final String ROLE_DESCRIPTION_REQUIRED = "Role description is required";
    public static final String USER_GROUP_NAME_ONLY_UPPER_CASE = "User group should contain only upper case";
    public static final String USER_GROUP_NAME_IS_NULL = "User group name is required";
    public static final String USER_GROUP_NOTE_IS_NULL = "User group note is required";
    public static final String USER_ID_REQUIRED = "User ID is required";
    public static final String PHONE_NUMBER_REQUIRED = "Phone number is required";
    public static final String GENDER_REQUIRED = "Gender is required";
    public static final String DOB_REQUIRED = "Date of birth is required";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String IDENTIFICATION_TYPE_REQUIRED = "First name is required";
    public static final String IDENTIFICATION_NUMBER_REQUIRED = "Identification number is required";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_ONLY_CHARACTERS = "Last name should contain only characters";
    public static final String FIRST_NAME_ONLY_CHARACTERS = "First name should contain only characters";
    public static final String INVALID_PHONE_NUMBER = "Invalid phone number";
    public static final String INVALID_SECOND_PHONE_NUMBER = "Invalid second phone number";
    public static final String INVALID_EMAIL = "Invalid email address";
    public static final String COUNTRY_ID_REQUIRED = "Country ID is required";
    public static final String INVALID_IDENTIFICATION_TYPE = "Invalid identification type: it should be OTHER, DRIVING_PERMIT, PASSPORT or NATIONAL_ID";
    public static final String NULL_PIN = "pin is null or empty";
    public static final String INVALID_PIN = "%s is an invalid pin, only numbers";
    public static final String PIN_BAD_LENGTH = "pin must be 4 characters";
    public static final String READ_PERMISSION = ".READ";
    public static final String WRITE_PERMISSION = ".WRITE";
    public static final String DELETE_PERMISSION = ".DELETE";
    public static final String UPDATE_PERMISSION = ".UPDATE";
    public static final String READ_DESCRIPTION = "READ PERMISSION";
    public static final String WRITE_DESCRIPTION = "WRITE PERMISSION";
    public static final String DELETE_DESCRIPTION = "DELETE PERMISSION";
    public static final String UPDATE_DESCRIPTION = "UPDATE PERMISSION";
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String USERNAME_TAKEN = "Username %s is already taken";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String USER_TYPE_REQUIRED = "User type is required";
    public static final String USERNAME_SHOULD_CONTAIN_ONLY_CHARS_AND_NUMBERS = "Username contains invalid characters";
    public static final String STANDARD_PASSWORD = "Password should be 8 to 50 characters long, with at least a digit, a lower case, an upper case and a special character";
    public static final String VALID_USER_TYPE = "Invalid userType: it should be ADMIN";
    public static final String VALID_SCHOOL_USER_TYPE = "Invalid userType: it should be DRIVER or SCHOOL_ADMIN or SCHOOL_STAFF";
    public static final String USER_GROUP_DOES_NOT_EXIST = "User group with ID: %s does not exist";
    public static final String PASSWORD_CANT_BE_UPDATED = "Password field cannot be updated";
    public static final String USER_DOES_NOT_EXIST = "User with ID: %s not found";
    public static final String NOT_ATTACHED_TO_SCHOOL = "User with ID: %s not attached to school";
    public static final String USER_WITH_PHONE_NUMBER_DOES_NOT_EXIST = "User with phone number: %s does not exist";
    public static final String USER_WITH_EMAIL_DOES_NOT_EXIST = "User with EMAIL: %s does not exist";
    public static final String USER_ACCOUNT_NOT_APPROVED = "User with ID: %s is not approved";
    public static final String USER_ACCOUNT_EXPIRED = "User account is expired";
    public static final String USER_ACCOUNT_DELETED = "User account is deleted";
    public static final String USER_ACCOUNT_CREDENTIALS_EXPIRED = "User account is deleted";
    public static final String USER_ACCOUNT_LOCKED = "User account is locked";
    public static final String PERMISSION_IS_IN_ACTIVE = "Permission is inactive";
    public static final String USER_ALREADY_ASSIGNED_TO_USER_GROUP = "User is already assigned to user group with ID: %s";
    public static final String EMAIL_ALREADY_TAKEN = "Email %s is already taken";
    public static final String PHONE_NUMBER_ALREADY_TAKEN = "Phone number %s is already taken";
    public static final String SECOND_PHONE_NUMBER_ALREADY_TAKEN = "Second phone number %s is already taken";
    public static final String APPROVAL_STATUS_REQUIRED = "Approval status is required";
    public static final String INVALID_APPROVAL_STATUS = "Invalid approval status: it should be APPROVED, REJECTED or PENDING";
    public static final String USER_APPROVAL_NOT_FOUND = "User approval with ID: %s not found";
    public static final String USER_ACCOUNT_IS_ALREADY_LOCKED = "User account is already locked";
    public static final String ROLE_EXISTS = "Role %s already exists";
    public static final String ROLE_DOES_NOT_EXIST = "Role with ID: %s does not exist";
    public static final String PERMISSION_DOES_NOT_EXIST = "Permission with ID: %s does not exist";
    public static final String PERMISSION_IS_ALREADY_IN_ACTIVE = "Permission with ID: %s is already inactive";
    public static final String PERMISSION_IS_ALREADY_ACTIVE = "Permission with ID: %s is already active";
    public static final String ROLE_NAME_DOES_NOT_EXIST = "Role with NAME: %s does not exist";
    public static final String PERMISSION_NAME_DOES_NOT_EXIST = "Permission with NAME: %s does not exist";
    public static final String PERMISSION_NOT_ASSIGNED_TO_USER_GROUP = "Permission %s, not assigned to user group %s";
    public static final String PERMISSION_ALREADY_ASSIGNED_TO_USER_GROUP = "Permission %s, already assigned to user group %s";
    public static final String USER_GROUP_ALREADY_EXISTS = "User group %s already exists";

}
