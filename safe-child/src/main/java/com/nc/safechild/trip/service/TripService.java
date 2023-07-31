package com.nc.safechild.trip.service;

import com.nc.safechild.exceptions.BadRequestException;
import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.trip.model.dto.TripRequestDto;
import com.nc.safechild.trip.model.dto.TripResponseDto;
import com.nc.safechild.student.models.enums.NotificationRoleEnum;
import com.nc.safechild.student.models.enums.StudentStatus;
import com.nc.safechild.trip.model.enums.TripStatus;
import com.nc.safechild.trip.model.enums.TripType;
import com.nc.safechild.student.models.jpa.StudentTravel;
import com.nc.safechild.trip.model.jpa.Trip;
import com.nc.safechild.student.repository.StudentTravelRepository;
import com.nc.safechild.trip.repository.TripRepository;
import com.nc.safechild.base.utils.DateTimeUtil;
import com.nc.safechild.base.utils.Validate;
import com.nc.safechild.base.utils.WebServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nc.safechild.base.utils.MessageConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/17/23
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final StudentTravelRepository studentTravelRepository;

    public List<TripResponseDto> getTripsByStaffUsername(String staffUsername, Pageable pageable){
        return tripRepository.findAllByStaffUsername(staffUsername, pageable).stream()
                .map(this::mapTripToTripResponseDto)
                .collect(Collectors.toList());
    }

    public TripResponseDto getExistingOpenOrInProgressTrip(String username){
        Optional<Trip> result;

        result = tripRepository.findByStaffUsernameAndTripStatus(username, TripStatus.IN_PROGRESS);

        if(result.isEmpty())
            result = tripRepository.findByStaffUsernameAndTripStatus(username, TripStatus.OPEN);

        Validate.isPresent(result, NO_OPEN_IN_PROGRESS_TRIPS, username);

        return mapTripToTripResponseDto(result.get());
    }

    public TripResponseDto createTrip(TripRequestDto tripRequestDto){

        tripRequestDto.validate();

        var existingInProgressTrip = tripRepository.findByStaffUsernameAndTripStatus(tripRequestDto.username(), TripStatus.IN_PROGRESS);
        Validate.isTrue(existingInProgressTrip.isEmpty(), ExceptionType.BAD_REQUEST, EXISTING_IN_PROGRESS_TRIP);

        var existingOpenTrip = tripRepository.findByStaffUsernameAndTripStatus(tripRequestDto.username(), TripStatus.OPEN);
        Validate.isTrue(existingOpenTrip.isEmpty(), ExceptionType.BAD_REQUEST, EXISTING_OPEN_TRIP);

        var user = WebServiceUtil.getUserByUsername(tripRequestDto.username());

        log.info(user.toString());
        var customFields = user.getFields();

        var schoolId = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("school_id"))
                .findFirst();

        Validate.isTrue(schoolId.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "school_id not found");

        Trip newTrip = Trip.builder()
                .tripType(TripType.valueOf(tripRequestDto.tripType()))
                .staffUsername(tripRequestDto.username())
                .tripStatus(TripStatus.OPEN)
                .schoolId(schoolId.get().getValue())
                .note(tripRequestDto.note())
                .build();

        newTrip.setCreatedOn(DateTimeUtil.getCurrentUTCTime());

        return mapTripToTripResponseDto(tripRepository.save(newTrip));
    }

    public TripResponseDto getTripById(Long id){

        var existingTrip = tripRepository.findById(id);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, id);
        var trip = existingTrip.get();

        return mapTripToTripResponseDto(trip);
    }

    public TripResponseDto changeTripStatus(Long id, TripStatus tripStatus){

        var existingTrip = tripRepository.findById(id);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, id);

        var trip = existingTrip.get();

        Validate.isTrue(!trip.getTripStatus().equals(tripStatus), ExceptionType.BAD_REQUEST, TRIP_SAME_STATUS, tripStatus);
        trip.setTripStatus(tripStatus);
        trip.setModifiedOn(DateTimeUtil.getCurrentUTCTime());

        return mapTripToTripResponseDto(tripRepository.save(trip));
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

        return mapTripToTripResponseDto(tripRepository.save(trip));
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
            if(studentTravelSignInList.stream().noneMatch(t -> t.getStudentUsername().equals(obj.getStudentUsername()))){
                pickUpList.add(obj);
            }
        }

        return pickUpList;
    }

    private List<StudentTravel> getStudentsOnDropOffTrip(Trip trip){

        var studentTravelSignOutList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.SCHOOL_SIGN_OUT);
        var studentTravelDropOffList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.HOME_DROP_OFF);
        var result = new ArrayList<StudentTravel>();

        for(StudentTravel obj : studentTravelSignOutList){
            if(studentTravelDropOffList.stream().noneMatch(t -> t.getStudentUsername().equals(obj.getStudentUsername()))){
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

    private List<NotificationRoleEnum> getTripRoles(TripType tripType){
        if(tripType.equals(TripType.PICK_UP)){
            return Arrays.asList(
                   NotificationRoleEnum.HOME_PICK_UP,
                   NotificationRoleEnum.BULK_ON_SCHOOL,
                   NotificationRoleEnum.SCHOOL_SIGN_IN
            );
        }

        if(tripType.equals(TripType.DROP_OFF)){
            return Arrays.asList(
                    NotificationRoleEnum.HOME_DROP_OFF,
                    NotificationRoleEnum.SCHOOL_SIGN_OUT
            );
        }
        throw new BadRequestException("Failed to load trip type");
    }

    private TripResponseDto mapTripToTripResponseDto(Trip trip){
        return new TripResponseDto(
                trip.getId(),
                trip.getCreatedOn(),
                trip.getModifiedOn(),
                trip.getTripType(),
                trip.getTripStatus(),
                trip.getStaffUsername(),
                getTripRoles(trip.getTripType())
        );
    }
}
