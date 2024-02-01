package com.nc.safechild.student.repository;

import com.nc.safechild.notification.model.jpa.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 6/29/23
 **/
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
