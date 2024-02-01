package com.bwongo.core.trip_mgt.model.dto;

import com.bwongo.core.base.model.enums.ReportStatus;
import com.bwongo.core.student_mgt.model.jpa.StudentTravel;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 1/11/24
 **/
@Setter
@Getter
public class StudentTripReportDto {
    TStudent student;
    StudentTravel firstStudentTravel;
    StudentTravel secondStudentTravel;
    ReportStatus status;
}
