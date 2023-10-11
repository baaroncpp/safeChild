package com.bwongo.core.trip_mgt.api;

import com.bwongo.core.trip_mgt.model.dto.StudentEventLocationDto;
import com.bwongo.core.trip_mgt.model.dto.TripResponseDto;
import com.bwongo.core.trip_mgt.service.TripService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/11/23
 **/
@Tag(name = "Trips",description = "Manage trips with the corresponding student event location coordinates on SafeChild")
@RestController
@RequestMapping("/api/v1/trip")
@RequiredArgsConstructor
public class TripApi {

    private final TripService tripService;

    @PreAuthorize("hasAnyAuthority('TRIP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "driver", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripResponseDto> getTripByDriverAndDate(@RequestParam(name = "page", required = true) int page,
                                                        @RequestParam(name = "size", required = true) int size,
                                                        @RequestParam(name = "username", required = true) String username,
                                                        @RequestParam(name = "fromDate", required = true) String fromDate,
                                                        @RequestParam(name = "toDate", required = true) String toDate){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return tripService.getTripsByDriverUsernameAndDate(username, fromDate, toDate, pageable);
    }

    @PreAuthorize("hasAnyAuthority('TRIP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "{id}/event-coordinates", produces = MediaType.APPLICATION_JSON_VALUE)
    List<StudentEventLocationDto> getEventCoordinates(@PathVariable("id") Long id){
        return tripService.getStudentEventLocation(id);
    }


}
