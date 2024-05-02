package com.ssafy.seoulpop.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
import com.ssafy.seoulpop.history.service.HistoryService;
import com.ssafy.seoulpop.notification.dto.NearestHistoryResponseDto;
import com.ssafy.seoulpop.notification.dto.NotificationRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final int H3_CHECK_LEVEL = 9;
    private static final double EARTH_RADIUS_M = 6371000.0;
    private final HistoryService historyService;

    public String createNotification(NotificationRequestDto notificationRequestDto) {
        List<NearByHistoryResponseDto> nearByHistoryList = historyService.readNearByHistoryList(notificationRequestDto.latitude(), notificationRequestDto.longitude(), H3_CHECK_LEVEL);

        if (nearByHistoryList.isEmpty()) {
            return "전송할 알림이 없습니다.";
        }

        NearestHistoryResponseDto nearestHistory = getNearestHistory(notificationRequestDto.latitude(), notificationRequestDto.longitude(), nearByHistoryList);

        //TODO: category별 알림 생성 필요
        String fcmToken = notificationRequestDto.fcmToken();
        Message message = Message.builder()
            .setToken(fcmToken)
            .setNotification(Notification.builder()
                .setTitle("근처에 새로운   역사 장소가 있습니다!")
                .setBody(nearestHistory.distance() + "m 떨어진 곳에 object가 위치!")
                //.setImage("이미지")
                .build())
            .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            return "알림이 전송되었습니다.";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "알림을 전송할 수 없습니다.";
        }
    }

    //TODO: redis에 알림 전송 시간을 담아두고, 계산해서 전송(하루에 한 번)
    public NearestHistoryResponseDto getNearestHistory(double memberLatitude, double memberLongitude, List<NearByHistoryResponseDto> nearByHistoryList) {
        double minDistance = Double.MAX_VALUE;
        NearByHistoryResponseDto nearestHistory = null;
        for (NearByHistoryResponseDto nearByHistory : nearByHistoryList) {
            double distance = calculateDistance(memberLatitude, memberLongitude, nearByHistory.lat(), nearByHistory.lng());

            if (distance < minDistance) {
                minDistance = distance;
                nearestHistory = nearByHistory;
            }
        }

        return NearestHistoryResponseDto.builder()
            .historyId(nearestHistory.id())
            .name(nearestHistory.name())
            .category(nearestHistory.category())
            .distance((int) minDistance)
            .build();
    }

    public double calculateDistance(double memberLatitude, double memberLongitude, double historyLat, double historyLng) {
        //위도와 경도 라디안 변환
        double memberLatRad = Math.toRadians(memberLatitude);
        double memberLngRad = Math.toRadians(memberLongitude);
        double historyLatRad = Math.toRadians(historyLat);
        double historyLngRad = Math.toRadians(historyLng);

        //위도 경도 차이 계산
        double latDifference = historyLatRad - memberLatRad;
        double lngDifference = historyLngRad - memberLngRad;

        //하버사인 공식 적용
        double intermediateValue = Math.pow(Math.sin(latDifference / 2), 2) + Math.cos(memberLatRad) * Math.cos(historyLatRad) * Math.pow(Math.sin(lngDifference / 2), 2);
        double centralAngle = 2 * Math.atan2(Math.sqrt(intermediateValue), Math.sqrt(1 - intermediateValue));

        // 최종 거리 계산 (미터 단위)
        return EARTH_RADIUS_M * centralAngle;
    }
}
