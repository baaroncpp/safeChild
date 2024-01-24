package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.user_mgt.model.jpa.TUserMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author bkaaron
 * @created 19/09/2022
 * @project kabangali
 */

@Repository
public interface TUserMetaRepository extends JpaRepository<TUserMeta, Long> {
    Optional<TUserMeta> findByPhoneNumber(String phoneNumber);
    Optional<TUserMeta> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber2(String phoneNumber2);
}
