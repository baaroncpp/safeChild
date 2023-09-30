package com.bwongo.core.vehicle_mgt.service;

import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.service.UserDtoService;
import com.bwongo.core.vehicle_mgt.model.dto.VehicleRequestDto;
import com.bwongo.core.vehicle_mgt.model.dto.VehicleResponseDto;
import com.bwongo.core.vehicle_mgt.model.jpa.TVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
@Service
@RequiredArgsConstructor
public class VehicleDtoService {

    private final UserDtoService userDtoService;
    private final SchoolDtoService schoolDtoService;

    public VehicleResponseDto vehicleToDto(TVehicle vehicle){

        if(vehicle == null){
            return null;
        }

        return new VehicleResponseDto(
                vehicle.getId(),
                vehicle.getCreatedOn(),
                vehicle.getModifiedOn(),
                userDtoService.tUserToDto(vehicle.getCreatedBy()),
                userDtoService.tUserToDto(vehicle.getModifiedBy()),
                userDtoService.tUserToDto(vehicle.getCurrentDriver()),
                vehicle.getPlateNumber(),
                vehicle.getVehicleModel(),
                vehicle.isOnRoute(),
                schoolDtoService.schoolToDto(vehicle.getSchool()),
                vehicle.getMaximumCapacity()
        );
    }

    public TVehicle dtoToTVehicle(VehicleRequestDto vehicleRequestDto){

        if(vehicleRequestDto == null){
            return null;
        }

        var school = new TSchool();
        school.setId(vehicleRequestDto.schoolId());

        var driver = new TUser();
        driver.setId(vehicleRequestDto.currentDriverId());

        var vehicle = new TVehicle();
        vehicle.setCurrentDriver(driver);
        vehicle.setPlateNumber(vehicleRequestDto.plateNumber());
        vehicle.setVehicleModel(vehicleRequestDto.vehicleModel());
        vehicle.setSchool(school);
        vehicle.setMaximumCapacity(vehicleRequestDto.maximumCapacity());

        return vehicle;
    }

}
