package com.bwongo.core.trip_mgt.repository;

import com.bwongo.core.base.model.enums.TripStatus;
import com.bwongo.core.trip_mgt.model.jpa.Trip;
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
 * @Date 7/17/23
 **/
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findBySchoolStaffAndTripStatus(TUser user, TripStatus tripStatus);
    List<Trip> findAllBySchoolStaff(TUser user, Pageable pageable);
    List<Trip> findAllBySchoolStaffAndCreatedOnBetween(TUser user, Date fromDate, Date toDate, Pageable pageable);
}
