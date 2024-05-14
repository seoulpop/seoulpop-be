package com.ssafy.seoulpop.fcm.repository;

import com.ssafy.seoulpop.fcm.domain.PushNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<PushNotification, Long> {

    List<PushNotification> findAllByMember_Id(long memberId);
}
