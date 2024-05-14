package com.ssafy.seoulpop.fcm.domain.client;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;

public interface FcmApiClient {

    @PostExchange(url = "https://fcm.googleapis.com/v1/projects/seoul-pop-85f8b/messages:send", contentType = APPLICATION_JSON_VALUE)
    String sendNotification(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String bearerToken, @RequestBody String fcmServerRequestDtoJson);
}
