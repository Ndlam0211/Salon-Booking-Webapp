package com.lamnd.service;

import com.lamnd.config.KeycloakConfig;
import com.lamnd.dto.identity.Credential;
import com.lamnd.dto.identity.KeycloakUserCreationRequest;
import com.lamnd.dto.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final RestTemplate restTemplate;
    private final KeycloakConfig keycloakConfig;

    public void register(RegistrationRequest request){
        String ACCESS_TOKEN = "";

        // map keycloak user create from request
        KeycloakUserCreationRequest keycloakUser = KeycloakUserCreationRequest.builder()
                .username(request.username())
                .email(request.email())
                .emailVerified(false)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .enabled(true)
                .credentials(List.of(Credential.builder()
                                .type("password")
                                .value(request.password())
                                .temporary(false)
                        .build()))
                .build();

        // create http request for create keycloak user
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<KeycloakUserCreationRequest> requestEntity = new HttpEntity<>(keycloakUser, headers);

        // call keycloak api to create a user
        ResponseEntity<String> response = restTemplate.exchange(
                keycloakConfig.getKeycloakAdminAPI(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

    }
}
