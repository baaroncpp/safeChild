package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.base.model.enums.ApprovalEnum;
import com.bwongo.core.user_mgt.model.jpa.TUserApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 2/7/23
 */
@Repository
public interface TUserApprovalRepository extends JpaRepository<TUserApproval, Long> {
    Optional<TUserApproval> findByUserId(Long userId);
    Page<TUserApproval> findAllByStatus(ApprovalEnum status, Pageable pageable);
}
