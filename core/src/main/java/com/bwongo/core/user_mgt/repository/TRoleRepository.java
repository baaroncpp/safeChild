package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.user_mgt.model.jpa.TRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Repository
public interface TRoleRepository extends JpaRepository<TRole, Long> {
    Optional<TRole> findByName(String name);
}
