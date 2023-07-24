package com.nc.safechild.trip.api;

import com.nc.safechild.trip.model.dto.TripRequestDto;
import com.nc.safechild.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping(path = "trips", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getTripsByUsername(@RequestParam("page") int page,
                                     @RequestParam("size") int size,
                                     @RequestParam("username") String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return tripService.getTripsByStaffUsername(username, pageable);
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
