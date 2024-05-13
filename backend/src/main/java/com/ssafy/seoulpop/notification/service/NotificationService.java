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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    //check level : 7 ~ 13
    private static final int H3_CHECK_LEVEL = 9;
    private static final double EARTH_RADIUS_M = 6371000.0;

    private final HistoryService historyService;
    private final StringRedisTemplate redisTemplate;

    public String createCookie(HttpServletResponse response, FcmCookieRequestDto requestDto) {
        log.debug("쿠키 발급 요청 접수, fcmToken: {}", requestDto.fcmToken());
        ResponseCookie cookie = ResponseCookie.from("fcmToken", requestDto.fcmToken())
            .path("/")
            .sameSite("None")
            .maxAge(30L * 24 * 60 * 60)
            .secure(true)
            .httpOnly(true)
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
        log.info("쿠키 발급 완료");
        return "쿠키 발급 완료";
    }

    public String sendNotification(HttpServletRequest request, NotificationRequestDto notificationRequest) {
        log.debug("알림 전송 요청 접수, noficationRequest : {}", notificationRequest.toString());

        if (!checkSendable(notificationRequest.memberId())) {
            return "알림 전송이 불가능합니다.";
        }

        List<NearByHistoryResponseDto> nearByHistoryList = historyService.readNearByHistoryList(notificationRequest.memberId(),
            notificationRequest.lat(), notificationRequest.lng(), H3_CHECK_LEVEL);
        log.debug("근처 역사 수 : {}", nearByHistoryList.size());

        if (nearByHistoryList.isEmpty()) {
            log.info("전송할 알림이 없어 종료되었습니다.");
            return "전송할 알림이 없습니다.";
        }

        Optional<NearestHistoryResponseDto> optionalHistory = findNearestHistory(notificationRequest, nearByHistoryList);
        if (optionalHistory.isEmpty()) {
            log.info("전송할 알림이 없어 종료되었습니다.");
            return "전송할 알림이 없습니다.";
        }
        NearestHistoryResponseDto nearestHistory = optionalHistory.get();

        String fcmToken = readFcmToken(request);

        Message message = createMessage(nearestHistory, fcmToken);

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            saveMessageInfo(notificationRequest.memberId(), nearestHistory.historyId(), response, message);
            log.info("알림이 전송되었습니다.");
            return "알림이 전송되었습니다.";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "알림을 전송할 수 없습니다.";
        }
    }

    private boolean checkSendable(long memberId) {
        LocalTime fromTime = LocalTime.of(21, 0);
        LocalTime toTime = LocalTime.of(9, 0);

        if (LocalTime.now().isAfter(fromTime) || LocalTime.now().isBefore(toTime)) {
            return false;
        }

        String checkKey = "notification:" + memberId;
        String lastNotification = redisTemplate.opsForValue().get(checkKey);
        if (lastNotification != null) {
            return false;
        }

        return true;
    }

    private Optional<NearestHistoryResponseDto> findNearestHistory(NotificationRequestDto notificationRequest, List<NearByHistoryResponseDto> nearByHistoryList) {
        double minDistance = Double.MAX_VALUE;
        NearByHistoryResponseDto nearestHistory = null;
        for (NearByHistoryResponseDto nearByHistory : nearByHistoryList) {
            if (!checkLocalSendable(notificationRequest.memberId(), nearByHistory.id())) {
                continue;
            }

            double distance = calculateDistance(notificationRequest.lat(), notificationRequest.lng(), nearByHistory.lat(), nearByHistory.lng());

            if (distance < minDistance) {
                minDistance = distance;
                nearestHistory = nearByHistory;
            }
        }

        if (nearestHistory == null) {
            return Optional.empty();
        }

        return Optional.of(NearestHistoryResponseDto.builder()
            .historyId(nearestHistory.id())
            .name(nearestHistory.name())
            .category(nearestHistory.category())
            .lat(nearestHistory.lat())
            .lng(nearestHistory.lng())
            .distance((int) minDistance)
            .build());
    }

    private boolean checkLocalSendable(long memberId, long historyId) {
        String checkKey = "notification:" + memberId + ":" + historyId;
        String lastNotification = redisTemplate.opsForValue().get(checkKey);
        if (lastNotification != null) {
            return false;
        }

        return true;
    }

    private double calculateDistance(double memberLatitude, double memberLongitude, double historyLat, double historyLng) {
        checkLocation(memberLatitude, memberLongitude);

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

    private void checkLocation(double lat, double lng) {
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new BaseException(ErrorCode.INVALID_LOCATION_ERROR);
        }
    }

    private String readFcmToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BaseException(ErrorCode.FCM_TOKEN_NOT_REGISTERED_ERROR);
        }

        return Arrays.stream(cookies)
            .filter(cookie -> "fcmToken".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(() -> new BaseException(ErrorCode.FCM_TOKEN_NOT_FOUND_ERROR));
    }

    private Message createMessage(NearestHistoryResponseDto nearestHistory, String fcmToken) {
        String notificationInfo = "아이디 : " + nearestHistory.historyId() + ", 이름 : " + nearestHistory.name() + ", 종류 : " + nearestHistory.category() + ", 거리 : " + nearestHistory.distance();
        log.debug("알림 정보 :{}", notificationInfo);

        StringBuilder messageBody = new StringBuilder(nearestHistory.distance() + "m 떨어진 곳에 ");
        switch (nearestHistory.category()) {
            case "3·1운동":
                messageBody.append("3·1운동의 흔적이 위치해 있습니다.");
                break;
            case "6·25전쟁":
                messageBody.append("6·25전쟁의 흔적이 위치해 있습니다.");
                break;
            default:
                messageBody.append("문화재가 위치해 있습니다.");
        }

        return Message.builder()
            .setToken(fcmToken)
            .setNotification(Notification.builder()
                .setTitle("근처에서 역사적 현장이 발견되었습니다!")
                .setBody(messageBody.toString())
                .build())
            .putData("historyId", String.valueOf(nearestHistory.historyId()))
            .putData("historyLat", String.valueOf(nearestHistory.lat()))
            .putData("historyLng", String.valueOf(nearestHistory.lng()))
            .build();
    }

    private void saveMessageInfo(long memberId, long historyId, String response, Message message) {
        //사용자별 하루 한 번 전송을 위한 저장
        redisTemplate.opsForValue().set("notification:" + memberId, String.valueOf(LocalDateTime.now()), Duration.ofHours(24));

        //장소별 하루 한 번 전송을 위한 저장
        redisTemplate.opsForValue().set("notification:" + memberId + ":" + historyId, String.valueOf(LocalDateTime.now()), Duration.ofHours(24));
    }
}
