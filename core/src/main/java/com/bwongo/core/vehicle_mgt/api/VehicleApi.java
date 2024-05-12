package com.bwongo.core.vehicle_mgt.api;

import com.bwongo.core.base.model.dto.PageResponseDto;
import com.bwongo.core.vehicle_mgt.model.dto.VehicleRequestDto;
import com.bwongo.core.vehicle_mgt.model.dto.VehicleResponseDto;
import com.bwongo.core.vehicle_mgt.service.VehicleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/30/23
 **/
@Tag(name = "Shuttles",description = "Manage shuttles on SafeChild")
@RestController
@RequestMapping("/api/v1/shuttle")
@RequiredArgsConstructor
public class VehicleApi {

    private final VehicleService vehicleService;

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('VEHICLE_ROLE.WRITE', 'ADMIN_ROLE.WRITE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleResponseDto addVehicle(@RequestBody VehicleRequestDto vehicleRequestDto){
        return vehicleService.addVehicle(vehicleRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('VEHICLE_ROLE.UPDATE', 'ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleResponseDto updateVehicle(@PathVariable("id") Long id,
                                            @RequestBody VehicleRequestDto vehicleRequestDto){
        return vehicleService.updateVehicle(id, vehicleRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('VEHICLE_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleResponseDto getVehicleById(@PathVariable("id") Long id){
        return vehicleService.getVehicleById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('VEHICLE_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDto getAllVehiclesBySchoolId(@RequestParam(name = "page", required = true) int page,
                                                    @RequestParam(name = "size",  required = true) int size,
                                                    @RequestParam(name = "schoolId", required = false) Long schoolId){
        var pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return vehicleService.getAllVehiclesBySchoolId(pageable, schoolId);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAnyAuthority('VEHICLE_ROLE.DELETE', 'ADMIN_ROLE.DELETE')")
    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteVehicle(@PathVariable("id") Long id){
        return vehicleService.deleteVehicle(id);
    }
}
