package com.nc.sms_notification.repository;

import com.nc.sms_notification.model.enums.SmsStatus;
import com.nc.sms_notification.model.jpa.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 6/29/23
 **/
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByStatus(SmsStatus status);
}
