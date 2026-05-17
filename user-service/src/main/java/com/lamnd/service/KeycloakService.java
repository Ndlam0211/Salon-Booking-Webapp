package com.lamnd.service;

import com.lamnd.config.KeycloakConfig;
import com.lamnd.dto.identity.*;
import com.lamnd.dto.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final RestTemplate restTemplate;
    private final KeycloakConfig keycloakConfig;

    public void register(RegistrationRequest request){
        String ACCESS_TOKEN = getAdminAccessToken().getAccessToken();

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

        // if create keycloak user successfully then assign role to the user
        if (response.getStatusCode() == HttpStatus.CREATED) {
            // fetch user just created to get user id
            KeycloakUserDTO user = fetchFirstUserByUsername(request.username(), ACCESS_TOKEN);

            // get role by name then assign to user
            KeycloakRole role = getRoleByName(keycloakConfig.getClientId(), ACCESS_TOKEN, request.role().toString());

            List<KeycloakRole> roles = new ArrayList<>();
            roles.add(role);

            // assign role to user
            assignRoleToUser(user.getId(),
                    keycloakConfig.getClientId(),
                    roles,
                    ACCESS_TOKEN);
        } else {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getBody());
        }

    }

    public TokenResponse getAdminAccessToken() {
        return new TokenResponse();
    }

    public KeycloakRole getRoleByName(String clientId, String token, String role) {
        return null;
    }

    public KeycloakUserDTO fetchFirstUserByUsername(String username, String token) {
        return null;
    }

    public void assignRoleToUser(String userId, String clientId, List<KeycloakRole> roles, String token) {

    }
}
