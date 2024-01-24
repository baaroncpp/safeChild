package com.bwongo.core.trip_mgt.utils;

import com.bwongo.core.base.model.enums.ReportStatus;
import com.bwongo.core.student_mgt.model.jpa.StudentTravel;
import com.bwongo.core.trip_mgt.model.dto.StudentTripReportDto;
import com.bwongo.core.trip_mgt.model.jpa.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.bwongo.core.base.model.enums.ReportStatus.*;
import static com.bwongo.core.base.model.enums.TripStatus.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/8/23
 **/
public class TripUtils {

    private TripUtils() {  }

    public static List<StudentTravel> getRemainingStudentsOnTrip(List<StudentTravel> onStudentList, List<StudentTravel> offStudentListList) {
        var remainingStudentList = new ArrayList<StudentTravel>();

        for(StudentTravel obj : onStudentList){
            if(offStudentListList.stream().noneMatch(t -> t.getStudent().getStudentUsername().equals(obj.getStudent().getStudentUsername()))){
                remainingStudentList.add(obj);
            }
        }

        return remainingStudentList;
    }

    public static List<StudentTripReportDto> generateStudentTripReport(Trip trip, List<StudentTravel> onStudentList, List<StudentTravel> offStudentListList){

        var studentTripReportList = new ArrayList<StudentTripReportDto>();

        for(StudentTravel studentTravel : onStudentList){
            var studentTripReportDto = getReportDto(offStudentListList, studentTravel);
            studentTripReportList.add(studentTripReportDto);
        }

        studentTripReportList.forEach(
                studentTripReportDto -> {
                    if(studentTripReportDto.getSecondStudentTravel() == null && trip.getTripStatus().equals(INCOMPLETE)){
                        studentTripReportDto.setStatus(FAILED);
                    }

                    if(studentTripReportDto.getSecondStudentTravel() == null && trip.getTripStatus().equals(IN_PROGRESS)){
                        studentTripReportDto.setStatus(PENDING);
                    }
                }
        );

        return studentTripReportList;
    }

    private static StudentTripReportDto getReportDto(List<StudentTravel> offStudentListList, StudentTravel studentTravel) {
        var studentTripReportDto = new StudentTripReportDto();

        studentTripReportDto.setStudent(studentTravel.getStudent());
        studentTripReportDto.setFirstStudentTravel(studentTravel);

        for(StudentTravel studentTravel2 : offStudentListList){
            if(Objects.equals(studentTravel.getStudent().getId(), studentTravel2.getStudent().getId())){
                studentTripReportDto.setSecondStudentTravel(studentTravel2);
                studentTripReportDto.setStatus(ReportStatus.SUCCESSFUL);
            }
        }
        return studentTripReportDto;
    }
}
