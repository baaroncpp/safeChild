package com.nc.safechild.student.respository;

import com.nc.safechild.student.model.enums.StudentStatus;
import com.nc.safechild.student.model.jpa.StudentTravel;
import com.nc.safechild.student.model.jpa.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
@Repository
public interface StudentTravelRepository extends JpaRepository<StudentTravel, Long> {
    Optional<StudentTravel> findByStudentUsernameAndTrip(String studentUsername, Trip trip);
    List<StudentTravel> findAllByTripAndStudentStatus(Trip trip, StudentStatus studentStatus);
}
