package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.user_mgt.model.jpa.TUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Repository
public interface TUserGroupRepository extends JpaRepository<TUserGroup, Long> {
    Optional<TUserGroup> findTUserGroupByName(String name);
}
