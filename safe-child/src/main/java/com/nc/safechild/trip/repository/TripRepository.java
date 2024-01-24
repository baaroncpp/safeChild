package com.nc.safechild.trip.repository;

import com.nc.safechild.trip.model.enums.TripStatus;
import com.nc.safechild.trip.model.jpa.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/17/23
 **/
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByStaffUsernameAndTripStatus(String driverUsername, TripStatus tripStatus);
    List<Trip> findAllByStaffUsername(String staffUsername, Pageable pageable);
}
