package com.nc.safechild.student.respository;

import com.nc.safechild.student.model.jpa.StudentDay;
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
}
