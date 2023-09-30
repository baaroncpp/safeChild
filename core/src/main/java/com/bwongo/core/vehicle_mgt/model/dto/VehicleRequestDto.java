package com.bwongo.core.vehicle_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.vehicle_mgt.utils.VehicleMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
public record VehicleRequestDto(
        Long currentDriverId,
        String plateNumber,
        String vehicleModel,
        Long schoolId,
        int maximumCapacity
) {
    public void validate(){
        Validate.notNull(currentDriverId, ExceptionType.BAD_REQUEST, NULL_CURRENT_DRIVER_ID);
        Validate.notEmpty(plateNumber, NULL_PLATE_NUMBER);
        Validate.notEmpty(vehicleModel, NULL_VEHICLE_MODEL);
        Validate.notNull(schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);
        Validate.isTrue((maximumCapacity > 0), ExceptionType.BAD_REQUEST, INVALID_MAX_CAPACITY);
    }
}
