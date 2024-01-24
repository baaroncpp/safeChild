package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.user_mgt.model.jpa.TGroupAuthority;
import com.bwongo.core.user_mgt.model.jpa.TPermission;
import com.bwongo.core.user_mgt.model.jpa.TUserGroup;
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
public interface TGroupAuthorityRepository extends JpaRepository<TGroupAuthority, Long> {
    List<TGroupAuthority> findByUserGroup(TUserGroup userGroup);
    Optional<TGroupAuthority> findByUserGroupAndPermission(TUserGroup userGroup, TPermission permission);
}
