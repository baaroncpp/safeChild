package com.bwongo.core.vehicle_mgt.repository;

import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.vehicle_mgt.model.jpa.TVehicle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
@Repository
public interface VehicleRepository extends JpaRepository<TVehicle, Long> {
    boolean existsByPlateNumber(String plateNumber);
    Optional<TVehicle> findByDeletedAndId(boolean isDeleted, Long id);
    List<TVehicle> findAllByDeletedAndSchool(Pageable pageable, boolean isDeleted, TSchool school);
}
