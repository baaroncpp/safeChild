package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.user_mgt.model.jpa.TAppClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author bkaaron
 * @created 16/06/2022
 * @project kabangali
 */
@Repository
public interface TAppClientRepository extends JpaRepository<TAppClient, Long> {
    Optional<TAppClient> findByName(String name);
}
