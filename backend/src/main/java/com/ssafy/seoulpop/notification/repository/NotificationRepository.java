package com.ssafy.seoulpop.notification.repository;

import com.ssafy.seoulpop.notification.domain.PushNotification;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<PushNotification, String> {

    Optional<PushNotification> findById(Long id);
    List<PushNotification> findAllByFcmToken(String fcmToken);
}
