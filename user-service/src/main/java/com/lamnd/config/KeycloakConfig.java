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
    private String keycloakClientId;

    @Value("${idp.client-secret}")
    private String keycloakClientSecret;

    @Value("${idp.url}/admin/realms/master/users")
    private String keycloakAdminAPI;

    @Value("${idp.url}/realms/master/protocol/openid-connect/token")
    private String keycloakTokenAPI;

    private final String grantType = "password";
    private final String scope = "openid profile email";
    private final String keycloakAdminUsername = "lamnd";
    private final String keycloakAdminPassword = "admin";
    private final String clientId = "75f89a62-25ce-422b-bb14-b762ba21ebf6";
}
