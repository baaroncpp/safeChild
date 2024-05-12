package com.bwongo.core.notify_mgt.repository;

import com.bwongo.core.notify_mgt.model.jpa.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/11/23
 **/
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByAccountNumberAndCreatedOnBetween(String accountNumber, Date startDate, Date endDate, Pageable pageable);
}
