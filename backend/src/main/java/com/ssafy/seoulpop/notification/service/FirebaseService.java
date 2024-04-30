package com.ssafy.seoulpop.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public String createNotification(String deviceToken, Map<String, Integer> nearbyInfo) {

        //TODO: 알림 유형 구분 필요(문화재, 역사)

        Message message = Message.builder()
            .setToken(deviceToken)
            .setNotification(Notification.builder()
                .setTitle("제목")
                .setBody(nearbyInfo.get("historyCnt") + "내용")
                .setImage("이미지")
                .build())
            .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            return "알림이 전송되었습니다.";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "알림을 전송할 수 없습니다.";
        }
    }
}
