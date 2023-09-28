package com.bwongo.core.student_mgt.repository;

import com.bwongo.core.student_mgt.model.jpa.TGuardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
@Repository
public interface GuardianRepository extends JpaRepository<TGuardian, Long> {
    Optional<TGuardian> findByDeletedAndId(boolean isDeleted, Long id);
    boolean existsByPhoneNumberAndDeleted(String phoneNumber, boolean isDeleted);
}
