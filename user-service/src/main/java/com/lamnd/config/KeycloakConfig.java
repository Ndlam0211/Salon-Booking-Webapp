package com.lamnd.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KeycloakConfig {

    @Value("${idp.url}")
    private String keycloakBaseURL;

    @Value("${idp.client-id}")
    private String keycloakClienId;

    @Value("${idp.client-secret}")
    private String keycloakClienSecret;

    private final String keycloakAdminAPI = keycloakBaseURL + "/admin/realms/master/users";
    private final String keycloakTokenAPI = keycloakBaseURL + "/realms/master/protocol/openid-connect/token";

    private final String grantType = "password";
    private final String scope = "openid profile email";
    private final String username = "lamnd";
    private final String password = "admin";
    private final String clientId = "9f7d7b6e-c8d5-40b6-8634-91c9e7650d06";
}
