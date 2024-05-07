package com.ssafy.seoulpop.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ssafy.seoulpop.exception.BaseException;
import com.ssafy.seoulpop.exception.ErrorCode;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
import com.ssafy.seoulpop.history.service.HistoryService;
import com.ssafy.seoulpop.notification.dto.FcmCookieRequestDto;
import com.ssafy.seoulpop.notification.dto.NearestHistoryResponseDto;
import com.ssafy.seoulpop.notification.dto.NotificationRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final int H3_CHECK_LEVEL = 9;
    private static final double EARTH_RADIUS_M = 6371000.0;

    private final HistoryService historyService;
    private final StringRedisTemplate redisTemplate;

    public String createCookie(HttpServletResponse response, FcmCookieRequestDto requestDto) {
        Cookie cookie = new Cookie("fcmToken", requestDto.fcmToken());
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);

        response.addCookie(cookie);

        return "쿠키 발급 완료";
    }

    public String createNotification(HttpServletRequest request, NotificationRequestDto notificationRequest) {
        List<NearByHistoryResponseDto> nearByHistoryList = historyService.readNearByHistoryList(notificationRequest.memberId(), notificationRequest.lat(), notificationRequest.lng(),
            H3_CHECK_LEVEL);

        if (nearByHistoryList.isEmpty()) {
            return "전송할 알림이 없습니다.";
        }

        NearestHistoryResponseDto nearestHistory = getNearestHistory(notificationRequest, nearByHistoryList);

        String fcmToken = Arrays.stream(request.getCookies())
            .filter(cookie -> "fcmToken".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(() -> new BaseException(ErrorCode.FCM_TOKEN_NOT_FOUND_ERROR));

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
            redisTemplate.opsForValue().set("notification:" + notificationRequest.memberId() + ":" + nearestHistory.historyId(), String.valueOf(LocalDate.now()));
            return "알림이 전송되었습니다.";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "알림을 전송할 수 없습니다.";
        }
    }

    public NearestHistoryResponseDto getNearestHistory(NotificationRequestDto notificationRequest, List<NearByHistoryResponseDto> nearByHistoryList) {
        double minDistance = Double.MAX_VALUE;
        NearByHistoryResponseDto nearestHistory = null;
        for (NearByHistoryResponseDto nearByHistory : nearByHistoryList) {
            if (!checkSendable(notificationRequest.memberId(), nearByHistory.id())) {
                continue;
            }

            double distance = calculateDistance(notificationRequest.lat(), notificationRequest.lng(), nearByHistory.lat(), nearByHistory.lng());

            if (distance < minDistance) {
                minDistance = distance;
                nearestHistory = nearByHistory;
            }
        }

        //TODO: nearest null일 때 처리
        return NearestHistoryResponseDto.builder()
            .historyId(nearestHistory.id())
            .name(nearestHistory.name())
            .category(nearestHistory.category())
            .distance((int) minDistance)
            .build();
    }

    public boolean checkSendable(Long memberId, Long historyId) {
        String checkKey = "notification:" + memberId + ":" + historyId;
        String lastNotification = redisTemplate.opsForValue().get(checkKey);
        if (lastNotification != null && LocalDate.parse(lastNotification).equals(LocalDate.now())) {
            return false;
        }

        return true;
    }

    public double calculateDistance(double memberLatitude, double memberLongitude, double historyLat, double historyLng) {
        //TODO: 위경도 유효성 검사

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
