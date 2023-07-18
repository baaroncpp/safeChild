package com.nc.safechild.student.respository;

import com.nc.safechild.student.model.enums.TripStatus;
import com.nc.safechild.student.model.jpa.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/17/23
 **/
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByDriverUsernameAndTripStatus(String driverUsername, TripStatus tripStatus);
}
