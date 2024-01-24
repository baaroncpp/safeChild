package com.bwongo.core.base.repository;

import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.model.jpa.TDistrict;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public interface TDistrictRepository  extends JpaRepository<TDistrict, Long> {
    List<TDistrict> findAllByCountry(TCountry country, Pageable pageable);
}
