package com.bwongo.core.trip_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.DateTimeUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.enums.*;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.student_mgt.model.jpa.StudentTravel;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.repository.StudentRepository;
import com.bwongo.core.student_mgt.repository.StudentTravelRepository;
import com.bwongo.core.trip_mgt.model.dto.StudentEventLocationDto;
import com.bwongo.core.trip_mgt.model.dto.TravelStudentDto;
import com.bwongo.core.trip_mgt.model.dto.TripRequestDto;
import com.bwongo.core.trip_mgt.model.dto.TripResponseDto;
import com.bwongo.core.trip_mgt.model.jpa.Trip;
import com.bwongo.core.trip_mgt.repository.TripRepository;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.repository.TUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bwongo.core.base.model.enums.TripStatus.*;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.*;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.bwongo.core.user_mgt.utils.UserMsgConstants.USERNAME_NOT_FOUND;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.USER_DOES_NOT_EXIST;
import static com.bwongo.core.vehicle_mgt.utils.VehicleMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/11/23
 **/
@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TripService {

    private final StudentRepository studentRepository;
    private final TUserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripDtoService tripDtoService;
    private final StudentTravelRepository studentTravelRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final AuditService auditService;

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public List<TripResponseDto> getTripsByDriverUsernameAndDate(String driverUsername,
                                                                 String fromStringDate,
                                                                 String toStringDate,
                                                                 Pageable pageable){

        var fromDate = getDateFromString(fromStringDate);
        var toDate = getDateFromString(toStringDate);

        var existingDriver = userRepository.findByUsername(driverUsername);
        Validate.isPresent(existingDriver, DRIVER_NOT_FOUND_BY_USERNAME, driverUsername);
        var driver = existingDriver.get();

        var driverTrips = tripRepository.findAllBySchoolStaffAndCreatedOnBetween(driver, fromDate, toDate, pageable);
        Validate.notNull(driverTrips, ExceptionType.BAD_REQUEST, NO_TRIPS_FOUND);

        return driverTrips.stream()
                .map(tripDtoService::tripToDto)
                .collect(Collectors.toList());
    }

    public List<StudentEventLocationDto> getStudentEventLocation(Long tripId){

        var existingTrip = tripRepository.findById(tripId);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, tripId);
        var trip = existingTrip.get();

        var studentTravels = studentTravelRepository.findAllByTrip(trip);
        Validate.notNull(studentTravels, ExceptionType.BAD_REQUEST, NO_STUDENT_COORDINATES);

        return studentTravels.stream()
                .map(tripDtoService::studentTravelToDto)
                .collect(Collectors.toList());
    }

    private Date getDateFromString(String stringDate){
        Validate.isAcceptableDateFormat(stringDate);
        return DateTimeUtil.stringToDate(stringDate, DATE_TIME_FORMAT);
    }

    private TStudent getStudentByUsername(String username){
        var student = studentRepository.findByStudentUsername(username);
        Validate.isPresent(student, STUDENT_NOT_FOUND_USERNAME, username);
        return student.get();
    }

    public List<TripResponseDto> getTripsByStaffUsername(Pageable pageable){
        var existingStaff = userRepository.findById(auditService.getLoggedInUser().getId());
        Validate.isPresent(existingStaff, USER_DOES_NOT_EXIST, auditService.getLoggedInUser().getId());
        var staff = existingStaff.get();

        return tripRepository.findAllBySchoolStaff(staff, pageable).stream()
                .map(tripDtoService::tripToDto)
                .collect(Collectors.toList());
    }

    public TripResponseDto getExistingOpenOrInProgressTrip(){
        Optional<Trip> result;

        var existingDriver = userRepository.findById(auditService.getLoggedInUser().getId());
        Validate.isPresent(existingDriver, USER_DOES_NOT_EXIST, auditService.getLoggedInUser().getId());
        var driver = existingDriver.get();

        result = tripRepository.findBySchoolStaffAndTripStatus(driver, IN_PROGRESS);

        if(result.isEmpty())
            result = tripRepository.findBySchoolStaffAndTripStatus(driver, TripStatus.OPEN);

        Validate.isPresent(result, NO_OPEN_IN_PROGRESS_TRIPS, driver.getUsername());

        return tripDtoService.tripToDto(result.get());
    }

    public TripResponseDto createTrip(TripRequestDto tripRequestDto){

        tripRequestDto.validate();
        final var driverId = auditService.getLoggedInUser().getId();

        var existingStaff = userRepository.findById(driverId);
        Validate.isPresent(existingStaff, DRIVER_NOT_FOUND, driverId);
        var staff = existingStaff.get();

        Validate.isTrue(staff.getUserType().equals(UserTypeEnum.DRIVER), ExceptionType.BAD_REQUEST, USER_NOT_DRIVER, driverId);

        var existingInProgressTrip = tripRepository.findBySchoolStaffAndTripStatus(staff, IN_PROGRESS);
        Validate.isTrue(existingInProgressTrip.isEmpty(), ExceptionType.BAD_REQUEST, EXISTING_IN_PROGRESS_TRIP);

        var existingOpenTrip = tripRepository.findBySchoolStaffAndTripStatus(staff, TripStatus.OPEN);
        Validate.isTrue(existingOpenTrip.isEmpty(), ExceptionType.BAD_REQUEST, EXISTING_OPEN_TRIP);

        var existingSchoolUser = schoolUserRepository.findByUser(staff);
        Validate.isPresent(existingSchoolUser, USER_DOES_NOT_BELONG_TO_SCHOOL, driverId);
        var schoolUser = existingSchoolUser.get();
        final var school = schoolUser.getSchool();

        Trip newTrip = Trip.builder()
                .tripType(TripType.valueOf(tripRequestDto.tripType()))
                .schoolStaff(staff)
                .tripStatus(TripStatus.OPEN)
                .school(school)
                .note(tripRequestDto.note())
                .build();

        auditService.stampAuditedEntity(newTrip);

        return tripDtoService.tripToDto(tripRepository.save(newTrip));
    }

    public TripResponseDto getTripById(Long id){

        var existingTrip = tripRepository.findById(id);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, id);
        var trip = existingTrip.get();

        return tripDtoService.tripToDto(trip);
    }

    public TripResponseDto changeTripStatus(Long id, TripStatus tripStatus){

        var existingTrip = tripRepository.findById(id);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, id);

        var trip = existingTrip.get();

        Validate.isTrue(!trip.getTripStatus().equals(tripStatus), ExceptionType.BAD_REQUEST, TRIP_SAME_STATUS, tripStatus);
        trip.setTripStatus(tripStatus);
        trip.setModifiedOn(DateTimeUtil.getCurrentUTCTime());

        return tripDtoService.tripToDto(tripRepository.save(trip));
    }

    public TripResponseDto endTrip(Long tripId){

        var trip = getTrip(tripId);

        List<StudentTravel> studentTravelOn;
        List<StudentTravel> studentTravelOff;

        if(trip.getTripType().equals(TripType.PICK_UP)){
            studentTravelOn = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.HOME_PICK_UP);
            studentTravelOff = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.SCHOOL_SIGN_IN);
        }else{
            studentTravelOn = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.SCHOOL_SIGN_OUT);
            studentTravelOff = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.HOME_DROP_OFF);
        }
        Validate.isTrue((studentTravelOn.size() == studentTravelOff.size()), ExceptionType.BAD_REQUEST, "students on trip");

        trip.setTripStatus(TripStatus.ENDED);
        trip.setModifiedOn(DateTimeUtil.getCurrentUTCTime());

        return tripDtoService.tripToDto(tripRepository.save(trip));
    }

    public List<StudentTravel> getStudentsCurrentlyOnTrip(Long tripId){

        var trip = getTrip(tripId);
        List<StudentTravel> studentTravelList;

        if(trip.getTripType().equals(TripType.PICK_UP)){
            studentTravelList = getStudentsOnPickUpTrip(trip);
        }else{
            studentTravelList = getStudentsOnDropOffTrip(trip);
        }

        return studentTravelList;
    }

    private List<StudentTravel> getStudentsOnPickUpTrip(Trip trip){

        var studentTravelPickUpList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.HOME_PICK_UP);
        var studentTravelSignInList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.SCHOOL_SIGN_IN);
        var pickUpList = new ArrayList<StudentTravel>();

        for(StudentTravel obj : studentTravelPickUpList){
            if(studentTravelSignInList.stream().noneMatch(t -> t.getStudent().getStudentUsername().equals(obj.getStudent().getStudentUsername()))){
                pickUpList.add(obj);
            }
        }

        return pickUpList;
    }

    public List<StudentTravel> getStudentsTripByStatus(TravelStudentDto travelStudentDto, Pageable pageable){

        travelStudentDto.validate();

        var existingTrip = tripRepository.findById(travelStudentDto.tripId());
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, travelStudentDto.tripId());

        var trip = existingTrip.get();

        var studentStatus = StudentStatus.valueOf(travelStudentDto.studentStatus());
        return studentTravelRepository.findAllByTripAndStudentStatus(trip, studentStatus, pageable);
    }

    private List<StudentTravel> getStudentsOnDropOffTrip(Trip trip){

        var studentTravelSignOutList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.SCHOOL_SIGN_OUT);
        var studentTravelDropOffList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.HOME_DROP_OFF);
        var result = new ArrayList<StudentTravel>();

        for(StudentTravel obj : studentTravelSignOutList){
            if(studentTravelDropOffList.stream().noneMatch(t -> t.getStudent().getStudentUsername().equals(obj.getStudent().getStudentUsername()))){
                result.add(obj);
            }
        }

        return result;
    }

    private Trip getTrip(Long id){
        var existingTrip = tripRepository.findById(id);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, id);

        var trip = existingTrip.get();
        Validate.isTrue(!trip.getTripStatus().equals(TripStatus.ENDED), ExceptionType.BAD_REQUEST, TRIP_ENDED);

        return trip;
    }
}
