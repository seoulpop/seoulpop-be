package com.ssafy.seoulpop.notification.service;

import com.ssafy.seoulpop.notification.dto.NotificationRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FirebaseService firebaseService;

    public String createAlert(NotificationRequest notificationRequest) {
        Map<String, Integer> nearbyInfo = checkNearBy(notificationRequest.latitude(), notificationRequest.longitude());
        int historyCnt = nearbyInfo.get("historyCnt");

        if (historyCnt == 0) {
            return "전송할 알림이 없습니다.";
        }

        //TODO: FCM 토큰 조회 필요

        String fcmToken = "";
        return firebaseService.createNotification(fcmToken, nearbyInfo);
    }

    public Map<String, Integer> checkNearBy(double latitude, double longitude) {
        return new HashMap<>();
    }
}
