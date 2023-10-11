package com.bwongo.core.trip_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.DateTimeUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.repository.StudentRepository;
import com.bwongo.core.trip_mgt.model.dto.StudentEventLocationDto;
import com.bwongo.core.trip_mgt.model.dto.TripResponseDto;
import com.bwongo.core.trip_mgt.repository.StudentTravelRepository;
import com.bwongo.core.trip_mgt.repository.TripRepository;
import com.bwongo.core.user_mgt.repository.TUserRepository;
import com.bwongo.core.vehicle_mgt.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private final VehicleRepository vehicleRepository;

    public List<TripResponseDto> getTripsByDriverUsernameAndDate(String driverUsername,
                                                                 String fromStringDate,
                                                                 String toStringDate,
                                                                 Pageable pageable){

        var fromDate = getDateFromString(fromStringDate);
        var toDate = getDateFromString(toStringDate);

        var existingDriver = userRepository.findByUsername(driverUsername);
        Validate.isPresent(existingDriver, DRIVER_NOT_FOUND_BY_USERNAME, driverUsername);
        var driver = existingDriver.get();

        var existingSchool = schoolUserRepository.findByUser(driver);
        Validate.isPresent(existingSchool, SCHOOL_NOT_FOUND);
        var school = existingSchool.get().getSchool();

        var driverTrips = tripRepository.findAllByStaffUsernameAndCreatedOnBetween(driverUsername, fromDate, toDate, pageable);
        Validate.notNull(driverTrips, ExceptionType.BAD_REQUEST, NO_TRIPS_FOUND);

        return driverTrips.stream()
                .map(trip -> tripDtoService.tripToDto(trip, driver, school))
                .collect(Collectors.toList());
    }

    public List<StudentEventLocationDto> getStudentEventLocation(Long tripId){

        var existingTrip = tripRepository.findById(tripId);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, tripId);
        var trip = existingTrip.get();

        var studentTravels = studentTravelRepository.findAllByTrip(trip);
        Validate.notNull(studentTravels, ExceptionType.BAD_REQUEST, NO_STUDENT_COORDINATES);

        return studentTravels.stream()
                .map(studentTravel -> tripDtoService.studentTravelToDto(studentTravel, getStudentByUsername(studentTravel.getStudentUsername())))
                .collect(Collectors.toList());
    }

    private Date getDateFromString(String stringDate){
        Validate.isAcceptableDateFormat(stringDate);
        return DateTimeUtil.stringToDate(stringDate, "yyyy-MM-dd HH:mm:ss");
    }

    private TStudent getStudentByUsername(String username){
        return studentRepository.findByStudentUsername(username).get();
    }
}
