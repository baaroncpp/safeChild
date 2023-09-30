package com.bwongo.core.vehicle_mgt.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.repository.SchoolRepository;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.repository.TUserRepository;
import com.bwongo.core.vehicle_mgt.model.dto.VehicleRequestDto;
import com.bwongo.core.vehicle_mgt.model.dto.VehicleResponseDto;
import com.bwongo.core.vehicle_mgt.model.jpa.TVehicle;
import com.bwongo.core.vehicle_mgt.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;
import static com.bwongo.core.vehicle_mgt.utils.VehicleMsgConstants.*;
import static com.bwongo.core.vehicle_mgt.utils.VehicleUtils.checkIfUserCanBeDriver;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final TUserRepository userRepository;
    private final VehicleDtoService vehicleDtoService;
    private final AuditService auditService;

    public VehicleResponseDto addVehicle(VehicleRequestDto vehicleRequestDto){

        vehicleRequestDto.validate();

        final var schoolId = vehicleRequestDto.schoolId();
        final var driverId = vehicleRequestDto.currentDriverId();
        final var plateNumber = vehicleRequestDto.plateNumber();

        Validate.isTrue(!vehicleRepository.existsByPlateNumber(plateNumber), ExceptionType.BAD_REQUEST, VEHICLE_PLATE_ALREADY_EXISTS, plateNumber);

        var expectedDriverSchool = getSchool(schoolId);

        var existingDriver = userRepository.findById(driverId);
        Validate.isPresent(existingDriver, DRIVER_NOT_FOUND, driverId);
        final var driver = existingDriver.get();

        var updatedVehicle = getVehicleResponseDto(vehicleRequestDto, schoolId, driverId, driver, expectedDriverSchool);

        return vehicleDtoService.vehicleToDto(vehicleRepository.save(updatedVehicle));
    }

    public VehicleResponseDto updateVehicle(Long id, VehicleRequestDto vehicleRequestDto){

        vehicleRequestDto.validate();
        var existingVehicle = getVehicle(id);
        final var schoolId = vehicleRequestDto.schoolId();
        final var driverId = vehicleRequestDto.currentDriverId();
        final var plateNumber = vehicleRequestDto.plateNumber();

        var driver = existingVehicle.getCurrentDriver();
        var expectedDriverSchool = existingVehicle.getSchool();

        Validate.isTrue(!existingVehicle.isOnRoute(), ExceptionType.BAD_REQUEST, VEHICLE_ON_TRIP, id);

        if(!existingVehicle.getPlateNumber().equals(plateNumber))
            Validate.isTrue(!vehicleRepository.existsByPlateNumber(plateNumber), ExceptionType.BAD_REQUEST, VEHICLE_PLATE_ALREADY_EXISTS, plateNumber);

        if(!Objects.equals(driver.getId(), driverId)) {
            var existingDriver = userRepository.findById(driverId);
            Validate.isPresent(existingDriver, DRIVER_NOT_FOUND, driverId);
            driver = existingDriver.get();
        }

        if(!Objects.equals(expectedDriverSchool.getId(), schoolId)){
            expectedDriverSchool = getSchool(schoolId);
        }

        var updatedVehicle = getVehicleResponseDto(vehicleRequestDto, schoolId, driverId, driver, expectedDriverSchool);
        updatedVehicle.setId(existingVehicle.getId());

        return vehicleDtoService.vehicleToDto(vehicleRepository.save(updatedVehicle));
    }

    public VehicleResponseDto getVehicleById(Long id){
        return vehicleDtoService.vehicleToDto(getVehicle(id));
    }

    public List<VehicleResponseDto> getAllVehiclesBySchoolId(Pageable pageable, Long schoolId){

        var school = getSchool(schoolId);

        var existingEditingUser = userRepository.findById(auditService.getLoggedInUser().getId());
        final var editingUser = existingEditingUser.get();

        if(!editingUser.getUserType().equals(UserTypeEnum.ADMIN))
            Validate.isTrue(schoolUserRepository.existsBySchoolAndUser(school, editingUser), ExceptionType.ACCESS_DENIED, CANT_ACCESS_SCHOOL, schoolId);


        return vehicleRepository.findAllByDeletedAndSchool(pageable, Boolean.FALSE, school).stream()
                .map(vehicleDtoService::vehicleToDto)
                .collect(Collectors.toList());
    }

    public boolean deleteVehicle(Long id){
        var existingVehicle = getVehicle(id);
        existingVehicle.setDeleted(Boolean.TRUE);
        auditService.stampAuditedEntity(existingVehicle);
        vehicleRepository.save(existingVehicle);

        return Boolean.TRUE;
    }

    private TVehicle getVehicleResponseDto(VehicleRequestDto vehicleRequestDto,
                                                     Long schoolId,
                                                     Long driverId,
                                                     TUser driver,
                                                     TSchool expectedDriverSchool) {
        Validate.isTrue(schoolUserRepository.existsBySchoolAndUser(expectedDriverSchool, driver), ExceptionType.BAD_REQUEST, DRIVER_DONT_BELONG_TO_SCHOOL, driverId, schoolId);
        checkIfUserCanBeDriver(driver);

        var vehicle = vehicleDtoService.dtoToTVehicle(vehicleRequestDto);
        auditService.stampAuditedEntity(vehicle);

        var existingEditingUser = userRepository.findById(vehicle.getCreatedBy().getId());
        final var editingUser = existingEditingUser.get();

        if(!editingUser.getUserType().equals(UserTypeEnum.ADMIN))
            Validate.isTrue(schoolUserRepository.existsBySchoolAndUser(expectedDriverSchool, editingUser), ExceptionType.ACCESS_DENIED, CANT_ASSIGN_SCHOOL, schoolId);

        vehicle.setSchool(expectedDriverSchool);
        vehicle.setCurrentDriver(driver);
        vehicle.setOnRoute(Boolean.FALSE);

        return vehicle;
    }

    private TVehicle getVehicle(Long id){
        var existingVehicle = vehicleRepository.findByDeletedAndId(Boolean.FALSE, id);
        Validate.isPresent(existingVehicle, VEHICLE_NOT_FOUND, id);
        return existingVehicle.get();
    }

    private TSchool getSchool(Long id){
        var existingSchool = schoolRepository.findById(id);
        Validate.isPresent(existingSchool, SCHOOL_NOT_FOUND, id);
        return existingSchool.get();
    }
}
