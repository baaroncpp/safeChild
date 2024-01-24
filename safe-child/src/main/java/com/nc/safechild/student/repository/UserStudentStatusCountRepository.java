package com.nc.safechild.student.repository;

import com.nc.safechild.student.models.enums.StudentStatus;
import com.nc.safechild.student.models.jpa.UserStudentStatusCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/6/23
 **/
@Repository
public interface UserStudentStatusCountRepository extends JpaRepository<UserStudentStatusCount, Long> {
    List<UserStudentStatusCount> findAllByUsernameAndDate(String username, Date date);
    Optional<UserStudentStatusCount> findByUsernameAndDateAndStudentStatus(String username, Date date, StudentStatus status);
}
