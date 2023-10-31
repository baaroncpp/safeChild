package com.bwongo.core.trip_mgt.utils;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/11/23
 **/
public class TripMsgConstants {

    private TripMsgConstants() { }

    public static final String NO_TRIPS_FOUND = "trips not found";
    public static final String TRIP_NOT_FOUND = "trip with ID: %s not found";
    public static final String NO_STUDENT_COORDINATES = "no student coordinates for this trip";
    public static final String NULL_TRIP_TYPE = "tripType is null or empty";
    public static final String NULL_TRIP_ID = "tripId is null or empty";
    public static final String INVALID_STUDENT_STATUS = "%s is invalid studentStatus: VALID OPTIONS: SCHOOL_SIGN_OUT, HOME_DROP_OFF, SCHOOL_SIGN_IN, HOME_PICK_UP, IN_CLASS";
    public static final String NULL_STUDENT_STATUS = "studentStatus is null or empty";
    public static final String INVALID_TRIP_TYPE = "Invalid tripType, options: PICK_UP, DROP_OFF";
    public static final String TRIP_ENDED = "trip is already ended";
    public static final String TRIP_SAME_STATUS = "trip already in %s status";
    public static final String EXISTING_OPEN_TRIP = "cannot create a new trip, OPEN trip exits";
    public static final String EXISTING_IN_PROGRESS_TRIP = "cannot create a new trip, IN_PROGRESS trip exits";
    public static final String NO_OPEN_IN_PROGRESS_TRIPS = "user: %s has no open or in progress trips";
    public static final String USER_DOES_NOT_BELONG_TO_SCHOOL = "User with ID: %s does not belong to any school";

}
