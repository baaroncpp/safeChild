package com.bwongo.core.trip_mgt.api;

import com.bwongo.core.student_mgt.model.dto.StudentResponseDto;
import com.bwongo.core.student_mgt.model.jpa.StudentTravel;
import com.bwongo.core.trip_mgt.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.bwongo.core.trip_mgt.model.dto.*;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/23/23
 **/

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TripMobileAppApi {

    private final TripService tripService;

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "trips", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripResponseDto> getTripsByUsername(@RequestParam("page") int page,
                                     @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return tripService.getTripsByStaffUsername(pageable);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "trip/student-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentTravel> getStudentsTripByStudentStatus(@RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              @RequestBody TravelStudentDto travelStudentDto){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return tripService.getStudentsTripByStatus(travelStudentDto, pageable);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "trips/open-and-in-progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripResponseDto getDriverOpenTrip(){
        return tripService.getExistingOpenOrInProgressTrip();
    }

    @Operation(summary = "Create new trip")
    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "trip", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TripResponseDto createNewTrip(@RequestBody TripRequestDto tripRequestDto){
        return tripService.createTrip(tripRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "trip/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripResponseDto getById(@PathVariable("id") Long id){
        return tripService.getTripById(id);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "students/trip/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentResponseDto> getStudentsOnTrip(@PathVariable("id") Long id){
        return tripService.getStudentsCurrentlyOnTrip(id);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "end/trip/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripResponseDto endTrip(@PathVariable("id") Long id){
        return tripService.endTrip(id);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "trip/report/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentTripReportResponseDto> getTripReport(@PathVariable("id") Long tripId){
        return tripService.getTripReport(tripId);
    }

    @PreAuthorize("hasAnyAuthority('TRIP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "driver", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentOnTripDto> getStudentsOnTrip(@RequestParam(name = "page", required = true) int page,
                                                    @RequestParam(name = "size", required = true) int size,
                                                    @RequestParam(name = "username", required = true) String username,
                                                    @RequestParam(name = "fromDate", required = true) String fromDate,
                                                    @RequestParam(name = "toDate", required = true) String toDate){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return tripService.getStudentsOnTripsByDriver(username, fromDate, toDate, pageable);
    }
}

