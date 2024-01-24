package com.bwongo.core.student_mgt.repository;

import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.student_mgt.model.jpa.UserStudentStatusCount;
import com.bwongo.core.user_mgt.model.jpa.TUser;
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
    List<UserStudentStatusCount> findAllByStaffAndDate(TUser user, Date date);
    Optional<UserStudentStatusCount> findByStaffAndDateAndStudentStatus(TUser user, Date date, StudentStatus status);
}
