package com.nc.safechild.student.service;

import com.nc.safechild.exceptions.BadRequestException;
import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.student.model.dto.TripRequestDto;
import com.nc.safechild.student.model.dto.TripResponseDto;
import com.nc.safechild.student.model.enums.NotificationRoleEnum;
import com.nc.safechild.student.model.enums.StudentStatus;
import com.nc.safechild.student.model.enums.TripStatus;
import com.nc.safechild.student.model.enums.TripType;
import com.nc.safechild.student.model.jpa.StudentTravel;
import com.nc.safechild.student.model.jpa.Trip;
import com.nc.safechild.student.respository.StudentTravelRepository;
import com.nc.safechild.student.respository.TripRepository;
import com.nc.safechild.utils.DateTimeUtil;
import com.nc.safechild.utils.Validate;
import com.nc.safechild.utils.WebServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nc.safechild.utils.MessageConstants.*;

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

    public TripResponseDto createTrip(TripRequestDto tripRequestDto){

        tripRequestDto.validate();

        var existingInProgressTrip = tripRepository.findByDriverUsernameAndTripStatus(tripRequestDto.username(), TripStatus.IN_PROGRESS);
        Validate.isTrue(existingInProgressTrip.isEmpty(), ExceptionType.BAD_REQUEST, EXISTING_IN_PROGRESS_TRIP);

        var existingOpenTrip = tripRepository.findByDriverUsernameAndTripStatus(tripRequestDto.username(), TripStatus.OPEN);
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
                .driverUsername(tripRequestDto.username())
                .tripStatus(TripStatus.OPEN)
                .schoolId(schoolId.get().getValue())
                .note(tripRequestDto.note())
                .build();

        newTrip.setCreatedOn(DateTimeUtil.getCurrentUTCTime());

        var savedTrip = tripRepository.save(newTrip);

        return new TripResponseDto(
                savedTrip.getId(),
                savedTrip.getCreatedOn(),
                savedTrip.getModifiedOn(),
                savedTrip.getTripType(),
                savedTrip.getTripStatus(),
                savedTrip.getDriverUsername(),
                getTripRoles(TripType.valueOf(tripRequestDto.tripType()))
        );
    }

    public TripResponseDto getTripById(Long id){

        var existingTrip = tripRepository.findById(id);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, id);
        var trip = existingTrip.get();

        return new TripResponseDto(
                trip.getId(),
                trip.getCreatedOn(),
                trip.getModifiedOn(),
                trip.getTripType(),
                trip.getTripStatus(),
                trip.getDriverUsername(),
                getTripRoles(trip.getTripType())
        );
    }

    public TripResponseDto changeTripStatus(Long id, TripStatus tripStatus){

        var existingTrip = tripRepository.findById(id);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, id);

        var trip = existingTrip.get();

        Validate.isTrue(!trip.getTripStatus().equals(tripStatus), ExceptionType.BAD_REQUEST, TRIP_SAME_STATUS, tripStatus);
        trip.setTripStatus(tripStatus);
        trip.setModifiedOn(DateTimeUtil.getCurrentUTCTime());

        var savedTrip = tripRepository.save(trip);

        return new TripResponseDto(
                savedTrip.getId(),
                savedTrip.getCreatedOn(),
                savedTrip.getModifiedOn(),
                savedTrip.getTripType(),
                savedTrip.getTripStatus(),
                savedTrip.getDriverUsername(),
                getTripRoles(savedTrip.getTripType())
        );
    }

    public Trip endTrip(Long tripId){

        var existingTrip = tripRepository.findById(tripId);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, tripId);

        var trip = existingTrip.get();
        Validate.isTrue(!trip.getTripStatus().equals(TripStatus.ENDED), ExceptionType.BAD_REQUEST, TRIP_ENDED);

        List<StudentTravel> studentTravelPickUp;

        if(trip.getTripType().equals(TripType.PICKUP)){
            studentTravelPickUp = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.PICK_UP);
        }else{
            studentTravelPickUp = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.OFF_SCHOOL);
        }
        Validate.isTrue(studentTravelPickUp.isEmpty(), ExceptionType.BAD_REQUEST, "students on trip");

        trip.setTripStatus(TripStatus.ENDED);
        trip.setModifiedOn(DateTimeUtil.getCurrentUTCTime());
        return tripRepository.save(trip);
    }

    private List<NotificationRoleEnum> getTripRoles(TripType tripType){
        if(tripType.equals(TripType.PICKUP)){
            return Arrays.asList(
                   NotificationRoleEnum.PICK_UP,
                   NotificationRoleEnum.BULK_ON_SCHOOL,
                   NotificationRoleEnum.ON_SCHOOL
            );
        }

        if(tripType.equals(TripType.DROP_OFF)){
            return Arrays.asList(
                    NotificationRoleEnum.DROP_OFF,
                    NotificationRoleEnum.OFF_SCHOOL
            );
        }
        throw new BadRequestException("Failed to load trip type");
    }
}
