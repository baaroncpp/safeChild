package com.bwongo.core.trip_mgt.repository;

import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.trip_mgt.model.jpa.StudentTravel;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bwongo.core.trip_mgt.model.jpa.Trip;
import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
@Repository
public interface StudentTravelRepository extends JpaRepository<StudentTravel, Long> {
    Optional<StudentTravel> findByStudentUsernameAndTripAndStudentStatus(String studentUsername, Trip trip, StudentStatus studentStatus);
    List<StudentTravel> findAllByTripAndStudentStatus(Trip trip, StudentStatus studentStatus, Pageable pageable);
    List<StudentTravel> findAllByTripAndStudentStatus(Trip trip, StudentStatus studentStatus);
    List<StudentTravel> findAllByTrip(Trip trip);
}
