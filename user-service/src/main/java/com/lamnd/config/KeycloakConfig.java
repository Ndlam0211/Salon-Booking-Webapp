package com.lamnd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakConfig {

    @Value("${idp.url}")
    private static String keycloakBaseURL;

    @Value("${idp.client-id}")
    private static String keycloakClienId;

    @Value("${idp.client-secret}")
    private static String keycloakClienSecret;

    private static final String KEYCLOAK_ADMIN_API = keycloakBaseURL + "/admin/realms/master/users";
    private static final String KEYCLOAK_TOKEN_API = keycloakBaseURL + "/realms/master/protocol/openid-connect/token";

    private static final String GRANT_TYPE = "password";
    private static final String SCOPE = "openid profile email";
    private static final String USERNAME = "lamnd";
    private static final String PASSWORD = "admin";
    private static final String CLIENT_ID = "9f7d7b6e-c8d5-40b6-8634-91c9e7650d06";
}
