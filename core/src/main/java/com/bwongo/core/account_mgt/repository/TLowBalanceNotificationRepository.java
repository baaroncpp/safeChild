package com.bwongo.core.account_mgt.repository;

import com.bwongo.core.account_mgt.model.jpa.TAccount;
import com.bwongo.core.account_mgt.model.jpa.TLowBalanceNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 1/9/24
 **/
@Repository
public interface TLowBalanceNotificationRepository extends JpaRepository<TLowBalanceNotification, Long> {
    Optional<TLowBalanceNotification> findTopBySchoolAccount(TAccount account);
}
