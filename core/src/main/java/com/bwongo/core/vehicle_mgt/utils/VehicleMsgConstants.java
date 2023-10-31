package com.bwongo.core.vehicle_mgt.utils;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
public class VehicleMsgConstants {

    private VehicleMsgConstants() { }

    public static final String NULL_CURRENT_DRIVER_ID = "currentDriverId is null or empty";
    public static final String NULL_PLATE_NUMBER = "plateNumber is null or empty";
    public static final String NULL_VEHICLE_MODEL = "vehicleModel is null or empty";
    public static final String PLATE_MUST_BE = "plateNumber must be 8 characters";
    public static final String INVALID_MAX_CAPACITY = "maximumCapacity must be greater than 0";
    public static final String NULL_SCHOOL_ID = "schoolId is null or empty";
    public static final String DRIVER_NOT_FOUND = "driver with ID: %s not found";
    public static final String DRIVER_WITH_USERNAME_FOUND = "driver with username: %s not found";
    public static final String USER_NOT_DRIVER = "user with ID: %s is not a driver";
    public static final String DRIVER_NOT_FOUND_BY_USERNAME = "driver with username: %s not found";
    public static final String DRIVER_DONT_BELONG_TO_SCHOOL = "driver ID: %s does not belong to school ID: %s";
    public static final String VEHICLE_PLATE_ALREADY_EXISTS = "vehicle with plate: %s already exists";
    public static final String CANT_ASSIGN_SCHOOL = "cannot assign school with ID: %s, does not match the school you belong";
    public static final String CANT_ACCESS_SCHOOL = "cannot access school with ID: %s, does not match the school you belong";
    public static final String VEHICLE_NOT_FOUND = "vehicle with ID: %s not found";
    public static final String VEHICLE_ON_TRIP = "vehicle with ID: %s is on route";
}
