package com.bwongo.core.student_mgt.utils;

import com.bwongo.core.student_mgt.model.jpa.TStudentGuardian;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/10/23
 **/
public class StudentManagementUtils {

    private StudentManagementUtils() { }

    public static boolean studentAlreadyHasNotifyingGuardian(List<TStudentGuardian> studentGuardians){

        if(studentGuardians.isEmpty())
            return Boolean.FALSE;

        for(TStudentGuardian studentGuardian : studentGuardians){
            if(studentGuardian.getGuardian().isNotified())
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
