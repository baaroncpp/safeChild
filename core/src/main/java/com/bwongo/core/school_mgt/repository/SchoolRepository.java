package com.bwongo.core.school_mgt.repository;

import com.bwongo.core.school_mgt.model.jpa.TSchool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
@Repository
public interface SchoolRepository extends JpaRepository<TSchool, Long> {
    Optional<TSchool> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    Page<TSchool> findAllByDeleted(boolean deleted, Pageable pageable);
}
