package com.bwongo.core.base.repository;

import com.bwongo.core.base.model.jpa.TLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
@Repository
public interface TLocationRepository extends JpaRepository<TLocation, Long> {
}
