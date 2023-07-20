package com.nc.safechild.student.api;

import com.nc.safechild.student.model.dto.TripRequestDto;
import com.nc.safechild.student.model.enums.TripStatus;
import com.nc.safechild.student.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TripApi {

    private final TripService tripService;

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

    @PutMapping(path = "trip", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object updateTripStatus(@Param("id") Long id,
                                   @Param("tripStatus")TripStatus tripStatus){
        return tripService.changeTripStatus(id, tripStatus);
    }

    @PutMapping(path = "end/trip/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object endTrip(@PathVariable("id") Long id){
        return tripService.endTrip(id);
    }
}
