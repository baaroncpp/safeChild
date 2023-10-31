package com.bwongo.core.trip_mgt.api;

import com.bwongo.core.trip_mgt.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.bwongo.core.trip_mgt.model.dto.*;

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

    @GetMapping(path = "trips", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getTripsByUsername(@RequestParam("page") int page,
                                     @RequestParam("size") int size,
                                     @RequestParam("username") String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return tripService.getTripsByStaffUsername(username, pageable);
    }

    @PostMapping(path = "trip/student-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getStudentsTripByStudentStatus(@RequestParam("page") int page,
                                                 @RequestParam("size") int size,
                                                 @RequestBody TravelStudentDto travelStudentDto){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return tripService.getStudentsTripByStatus(travelStudentDto, pageable);
    }

    @GetMapping(path = "trip/driver/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getDriverOpenTrip(@PathVariable("username") String username){
        return tripService.getExistingOpenOrInProgressTrip(username);
    }

    @PostMapping(path = "trip", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createNewTrip(@RequestBody TripRequestDto tripRequestDto){
        return tripService.createTrip(tripRequestDto);
    }

    @GetMapping(path = "trip/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getById(@PathVariable("id") Long id){
        return tripService.getTripById(id);
    }

    @GetMapping(path = "students/trip/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getStudentsOnTrip(@PathVariable("id") Long id){
        return tripService.getStudentsCurrentlyOnTrip(id);
    }

    @PutMapping(path = "end/trip/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object endTrip(@PathVariable("id") Long id){
        return tripService.endTrip(id);
    }
}

