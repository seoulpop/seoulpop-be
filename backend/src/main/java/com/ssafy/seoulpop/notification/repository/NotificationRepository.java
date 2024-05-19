package com.ssafy.seoulpop.notification.repository;

import com.ssafy.seoulpop.notification.domain.PushNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<PushNotification, String> {

}
