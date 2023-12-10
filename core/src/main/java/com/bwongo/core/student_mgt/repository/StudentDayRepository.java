package com.bwongo.core.student_mgt.repository;

import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.student_mgt.model.jpa.StudentDay;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/19/23
 **/
@Repository
public interface StudentDayRepository extends JpaRepository<StudentDay, Long> {
    Optional<StudentDay> findBySchoolDateAndStudentAndSchool(Date schoolDate, TStudent student, TSchool school);
    List<StudentDay> findAllByStaffAndSchoolDate(TUser staff, Date schoolDate, Pageable pageable);
    List<StudentDay> findAllBySchoolAndStudentStatusAndSchoolDate(TSchool school, StudentStatus studentStatus, Date schoolDate, Pageable pageable);
    List<StudentDay> findBySchoolDateAndStudent(Date schoolDate, TStudent student);
    Optional<StudentDay> findBySchoolDateAndStudentAndStudentStatus(Date schoolDate, TStudent student, StudentStatus studentStatus);
}
