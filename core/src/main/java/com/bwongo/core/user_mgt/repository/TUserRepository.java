package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Repository
public interface TUserRepository extends JpaRepository<TUser, Long> {
    Optional<TUser> findByUsername(String username);
    long countByUserType(UserTypeEnum userType);

    boolean existsByUsername(String username);
}
