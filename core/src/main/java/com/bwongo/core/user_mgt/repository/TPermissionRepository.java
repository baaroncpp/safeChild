package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.user_mgt.model.jpa.TPermission;
import com.bwongo.core.user_mgt.model.jpa.TRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Repository
public interface TPermissionRepository extends JpaRepository<TPermission, Long> {
    List<TPermission> findByAssignableEquals(Boolean isAssignable);
    Optional<TPermission> findByName(String name);
    List<TPermission> findAllByRole(TRole role);
}
