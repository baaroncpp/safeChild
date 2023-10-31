package com.bwongo.core.trip_mgt.service;

import com.bwongo.commons.models.exceptions.BadRequestException;
import com.bwongo.core.base.model.enums.NotificationRoleEnum;
import com.bwongo.core.base.model.enums.TripType;
import com.bwongo.core.base.service.BaseDtoService;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.student_mgt.model.jpa.StudentTravel;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.service.StudentDtoService;
import com.bwongo.core.trip_mgt.model.dto.StudentEventLocationDto;
import com.bwongo.core.trip_mgt.model.dto.TripResponseDto;
import com.bwongo.core.trip_mgt.model.jpa.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/11/23
 **/
@RequiredArgsConstructor
@Service
public class TripDtoService {

    private final SchoolDtoService schoolDtoService;
    private final StudentDtoService studentDtoService;
    private final BaseDtoService baseDtoService;

    public TripResponseDto tripToDto(Trip trip){

        if(trip == null){
            return null;
        }

        return new TripResponseDto(
                trip.getId(),
                trip.getCreatedOn(),
                trip.getModifiedOn(),
                trip.getTripType(),
                trip.getTripStatus(),
                schoolDtoService.tUserToUserSchoolDto(trip.getSchoolStaff(), trip.getSchool())
        );
    }

    public StudentEventLocationDto studentTravelToDto(StudentTravel studentTravel){

        if(studentTravel == null){
            return null;
        }

        return new StudentEventLocationDto(
                studentTravel.getId(),
                studentTravel.getCreatedOn(),
                studentTravel.getModifiedOn(),
                studentTravel.getStudentStatus(),
                studentDtoService.studentToDto(studentTravel.getStudent()),
                baseDtoService.locationToDto(studentTravel.getLocation())
        );
    }

    private List<NotificationRoleEnum> getTripRoles(TripType tripType) {
        if (tripType.equals(TripType.PICK_UP)) {
            return Arrays.asList(
                    NotificationRoleEnum.HOME_PICK_UP,
                    NotificationRoleEnum.BULK_ON_SCHOOL,
                    NotificationRoleEnum.SCHOOL_SIGN_IN
            );
        }

        if (tripType.equals(TripType.DROP_OFF)) {
            return Arrays.asList(
                    NotificationRoleEnum.HOME_DROP_OFF,
                    NotificationRoleEnum.SCHOOL_SIGN_OUT
            );
        }
        throw new BadRequestException("Failed to load trip type");
    }
}
