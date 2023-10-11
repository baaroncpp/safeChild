package com.bwongo.core.trip_mgt.service;

import com.bwongo.core.base.service.BaseDtoService;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.service.StudentDtoService;
import com.bwongo.core.trip_mgt.model.dto.StudentEventLocationDto;
import com.bwongo.core.trip_mgt.model.dto.TripResponseDto;
import com.bwongo.core.trip_mgt.model.jpa.StudentTravel;
import com.bwongo.core.trip_mgt.model.jpa.Trip;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public TripResponseDto tripToDto(Trip trip, TUser user, TSchool school){

        if(trip == null){
            return null;
        }

        return new TripResponseDto(
                trip.getId(),
                trip.getCreatedOn(),
                trip.getModifiedOn(),
                trip.getTripType(),
                trip.getTripStatus(),
                schoolDtoService.tUserToUserSchoolDto(user, school)
        );
    }

    public StudentEventLocationDto studentTravelToDto(StudentTravel studentTravel, TStudent student){

        if(studentTravel == null){
            return null;
        }

        return new StudentEventLocationDto(
                studentTravel.getId(),
                studentTravel.getCreatedOn(),
                studentTravel.getModifiedOn(),
                studentTravel.getStudentStatus(),
                studentDtoService.studentToDto(student),
                baseDtoService.locationToDto(studentTravel.getLocation())
        );
    }
}
