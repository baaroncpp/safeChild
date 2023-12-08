package com.bwongo.core.trip_mgt.utils;

import com.bwongo.core.student_mgt.model.jpa.StudentTravel;

import java.util.ArrayList;
import java.util.List;

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
}
