package com.ssafy.seoulpop.fcm.domain.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceAccountKey {

    @Value("${firebase.type}")
    private String type;

    @Value("${firebase.project_id}")
    private String projectId;

    @Value("${firebase.private_key_id}")
    private String privateKeyId;

    @Value("${firebase.private_key}")
    private String privateKey;

    @Value("${firebase.client_email}")
    private String clientEmail;

    @Value("${firebase.client_id}")
    private String clientId;

    @Value("${firebase.auth_uri}")
    private String authUri;

    @Value("${firebase.token_uri}")
    private String tokenUri;

    @Value("${firebase.auth_provider_x509_cert_url}")
    private String authProviderX509CertUrl;

    @Value("${firebase.client_x509_cert_url}")
    private String clientX509CertUrl;

    @Value("${firebase.universe_domain}")
    private String universeDomain;
}
