package com.bwongo.core.trip_mgt.model.dto;

import com.bwongo.core.base.model.enums.ReportStatus;
import com.bwongo.core.student_mgt.model.dto.StudentResponseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 1/10/24
 **/
public record StudentTripReportResponseDto(
        StudentResponseDto student,
        StudentEventLocationDto firstStudentTravel,
        StudentEventLocationDto secondStudentTravel,
        ReportStatus status
) {

}
