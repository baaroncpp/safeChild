package com.bwongo.core.base.repository;

import com.bwongo.core.base.model.jpa.TCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
@Repository
public interface TCountryRepository  extends JpaRepository<TCountry, Long> {
    Optional<TCountry> findByCountryCode(Integer countryCode);
    boolean existsByCountryCode(int countryCOde);
    boolean existsByIsoAlpha2(String isoAlpha2);
    boolean existsByIsoAlpha3(String isoAlpha2);
}
