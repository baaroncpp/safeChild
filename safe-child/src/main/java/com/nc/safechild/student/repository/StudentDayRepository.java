package com.nc.safechild.student.repository;

import com.nc.safechild.student.models.enums.StudentStatus;
import com.nc.safechild.student.models.jpa.StudentDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/19/23
 **/
@Repository
public interface StudentDayRepository extends JpaRepository<StudentDay, Long> {
    Optional<StudentDay> findBySchoolDateAndStudentUsernameAndSchoolId(Date schoolDate, String studentUsername, String schoolId);
    Optional<StudentDay> findBySchoolDateAndStudentUsername(Date schoolDate, String studentUsername);
    Optional<StudentDay> findBySchoolDateAndStudentUsernameAndStudentStatus(Date schoolDate, String studentUsername, StudentStatus studentStatus);
}
