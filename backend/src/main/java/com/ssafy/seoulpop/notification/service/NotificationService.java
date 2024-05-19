package com.ssafy.seoulpop.notification.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssafy.seoulpop.exception.BaseException;
import com.ssafy.seoulpop.exception.ErrorCode;
import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
import com.ssafy.seoulpop.history.repository.HistoryRepository;
import com.ssafy.seoulpop.history.service.HistoryService;
import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.repository.MemberRepository;
import com.ssafy.seoulpop.notification.domain.PushNotification;
import com.ssafy.seoulpop.notification.domain.account.ServiceAccountKey;
import com.ssafy.seoulpop.notification.domain.client.FcmApiClient;
import com.ssafy.seoulpop.notification.dto.CookieRequestDto;
import com.ssafy.seoulpop.notification.dto.FcmRequestDto;
import com.ssafy.seoulpop.notification.dto.FcmRequestDto.Data;
import com.ssafy.seoulpop.notification.dto.NearestHistoryResponseDto;
import com.ssafy.seoulpop.notification.dto.NotificationRequestDto;
import com.ssafy.seoulpop.notification.dto.NotificationResponseDto;
import com.ssafy.seoulpop.notification.dto.UpdateRequestDto;
import com.ssafy.seoulpop.notification.repository.NotificationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    //check level : 7 ~ 13
    private static final int H3_CHECK_LEVEL = 9;
    private static final double EARTH_RADIUS_M = 6371000.0;
    private static final long COOKIE_AGE = 30L * 24 * 60 * 60;

    private final FcmApiClient fcmApiClient;
    private final ServiceAccountKey serviceAccountKey;
    private final RedisTemplate<String, Object> fcmRedisTemplate;
    private final HistoryService historyService;
    private final HistoryRepository historyRepository;
    private final NotificationRepository notificationRepository;

    public String createCookie(HttpServletResponse response, CookieRequestDto requestDto) {
        String fcmToken = requestDto.fcmToken();

        if(fcmToken == "" || fcmToken == null) {
            throw new BaseException(ErrorCode.FCM_TOKEN_NOT_FOUND_ERROR);
        }

        ResponseCookie cookie = ResponseCookie.from("fcmToken", fcmToken)
            .path("/")
            .sameSite("None")
            .maxAge(COOKIE_AGE)
            .secure(true)
            .httpOnly(true)
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
        log.info("쿠키 발급 완료");
        return "쿠키 발급 완료";
    }

    public String sendNotification(HttpServletRequest request, NotificationRequestDto notificationRequest) throws IOException {

        log.info("알림 전송 요청");
        String fcmToken = getFcmToken(request);

        //if (!checkSendable(fcmToken)) {
        //  return "알림 전송이 불가능합니다.";
        //}

        log.info("인접 역사 리스트 반환");
        List<NearByHistoryResponseDto> nearByHistoryList = historyService.readNearByHistoryList(notificationRequest.lat(), notificationRequest.lng(), H3_CHECK_LEVEL);

        if (nearByHistoryList.isEmpty()) {
            log.info("전송할 알림이 없어 종료되었습니다.");
            return "전송할 알림이 없습니다.";
        }

        log.info("가장 가까운 역사 반환");
        Optional<NearestHistoryResponseDto> optionalHistory = findNearestHistory(fcmToken, notificationRequest, nearByHistoryList);
        if (optionalHistory.isEmpty()) {
            log.info("전송할 알림이 없어 종료되었습니다.");
            return "전송할 알림이 없습니다.";
        }
        NearestHistoryResponseDto nearestHistory = optionalHistory.get();

        log.info("메세지 생성");
        FcmRequestDto message = createMessage(nearestHistory, fcmToken);


        Gson gson = new Gson();
        fcmApiClient.sendNotification("Bearer " + getAccessToken(), gson.toJson(message));

        saveMessageInfo(fcmToken, nearestHistory.historyId(), message);

        log.info("알림이 전송되었습니다.");
        return "알림이 전송되었습니다.";
    }

    public List<NotificationResponseDto> readNotificationList(HttpServletRequest request) {
        List<PushNotification> findNotificationList = notificationRepository.findAllByFcmToken(getFcmToken(request));

        List<NotificationResponseDto> resultList = new ArrayList<>();
        for (PushNotification notification : findNotificationList) {
            resultList.add(
                NotificationResponseDto.builder()
                    .notificationId(notification.getId())
                    .body(notification.getBody())
                    .title(notification.getTitle())
                    .historyId(notification.getHistory().getId())
                    .historyName(notification.getHistory().getName())
                    .historyCategory(notification.getHistory().getCategory())
                    .historyLat(notification.getHistory().getLat())
                    .historyLng(notification.getHistory().getLng())
                    .checked(notification.getChecked())
                    .build()
            );
        }

        return resultList;
    }

    @Transactional
    public String updateNotification(UpdateRequestDto requestDto) {
        PushNotification findNotification = notificationRepository.findById(requestDto.notificationId()).orElseThrow(() -> new BaseException(ErrorCode.NOTIFICATION_NOT_FOUND_ERROR));
        if (!findNotification.getChecked()) {
            findNotification.updateChecked();
        }

        return "알림 확인 여부가 업데이트되었습니다.";
    }

    private String getFcmToken(HttpServletRequest request) {
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

    private boolean checkSendable(String fcmToken) {
        LocalTime fromTime = LocalTime.of(21, 0);
        LocalTime toTime = LocalTime.of(9, 0);

        if (LocalTime.now().isAfter(fromTime) || LocalTime.now().isBefore(toTime)) {
            return false;
        }

        String lastNotification = (String) fcmRedisTemplate.opsForValue().get(fcmToken);
        if (lastNotification != null) {
            return false;
        }

        return true;
    }

    private Optional<NearestHistoryResponseDto> findNearestHistory(String fcmToken, NotificationRequestDto notificationRequest, List<NearByHistoryResponseDto> nearByHistoryList) {
        double minDistance = Double.MAX_VALUE;
        NearByHistoryResponseDto nearestHistory = null;
        for (NearByHistoryResponseDto nearByHistory : nearByHistoryList) {
            //if (!checkLocalSendable(fcmToken, nearByHistory.id())) {
            //    continue;
            //}

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

    private boolean checkLocalSendable(String fcmToken, Long historyId) {
        String checkKey = fcmToken + ":" + historyId;
        String lastNotification = (String) fcmRedisTemplate.opsForValue().get(checkKey);
        if (lastNotification != null) {
            return false;
        }

        return true;
    }

    private double calculateDistance(Double memberLatitude, Double memberLongitude, Double historyLat, Double historyLng) {
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

    private void checkLocation(Double lat, Double lng) {
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new BaseException(ErrorCode.INVALID_LOCATION_ERROR);
        }
    }

    private FcmRequestDto createMessage(NearestHistoryResponseDto nearestHistory, String fcmToken) {
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

        return FcmRequestDto.builder()
            .message(FcmRequestDto.Message.builder()
                .token(fcmToken)
                .data(Data.builder()
                    .notificationId(UUID.randomUUID().toString())
                    .historyId(String.valueOf(nearestHistory.historyId()))
                    .historyName(nearestHistory.name())
                    .historyCategory(nearestHistory.category())
                    .historyLat(String.valueOf(nearestHistory.lat()))
                    .historyLng(String.valueOf(nearestHistory.lng()))
                    .build())
                .notification(FcmRequestDto.Notification.builder()
                    .title("근처에서 역사적 현장이 발견되었습니다!")
                    .body(messageBody.toString())
                    .build())
                .build())
            .build();
    }

    private String getAccessToken() throws IOException {
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();

        String jsonString = gson.toJson(serviceAccountKey);
        InputStream serviceAccount = new ByteArrayInputStream(jsonString.getBytes());

        GoogleCredentials googleCredentials = GoogleCredentials
            .fromStream(serviceAccount)
            .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private void saveMessageInfo(String fcmToken, Long historyId, FcmRequestDto requestDto) {
        //사용자별 하루 한 번 전송을 위한 저장
        fcmRedisTemplate.opsForValue().set(fcmToken, String.valueOf(LocalDateTime.now()), Duration.ofHours(24));

        //장소별 하루 한 번 전송을 위한 저장
        fcmRedisTemplate.opsForValue().set(fcmToken + ":" + historyId, String.valueOf(LocalDateTime.now()), Duration.ofHours(24));

        //알림 내용 저장
        History findHistory = historyRepository.findById(historyId).orElseThrow(() -> new BaseException(ErrorCode.HISTORY_NOT_FOUND_ERROR));

        notificationRepository.save(
            PushNotification.builder()
                .fcmToken(fcmToken)
                .history(findHistory)
                .body(requestDto.message().notification().body())
                .title(requestDto.message().notification().title())
                .checked(false)
                .build()
        );
    }
}
