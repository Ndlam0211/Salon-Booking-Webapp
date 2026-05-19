package com.lamnd.service;

import com.lamnd.config.KeycloakConfig;
import com.lamnd.dto.identity.*;
import com.lamnd.dto.request.RegistrationRequest;
import com.lamnd.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final RestTemplate restTemplate;
    private final KeycloakConfig keycloakConfig;

    public void register(RegistrationRequest request){
        String ACCESS_TOKEN = getAccessToken(keycloakConfig.getKeycloakAdminUsername(),
                keycloakConfig.getKeycloakAdminPassword(), keycloakConfig.getGrantType(), null).getAccessToken();

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

        log.info("Create user response from Keycloak: {}", response);

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

    public TokenResponse getAccessToken(String username,
                                             String password,
                                             String grantType,
                                             String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> requestEntity = getMultiValueMapHttpEntity(headers,
                username, password, grantType, refreshToken);

        // call keycloak api to get access token
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                keycloakConfig.getKeycloakTokenAPI(),
                HttpMethod.POST,
                requestEntity,
                TokenResponse.class
        );

        log.info("Get access token response from Keycloak: {}", response);

        // if get access token successfully then return the token response, otherwise throw an exception
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get access token from Keycloak: " + response.getBody());
        }
    }

    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(HttpHeaders headers,
                                                                                 String username,
                                                                                 String password,
                                                                                 String grantType,
                                                                                 String refreshToken) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        if (keycloakConfig.getKeycloakClientId() != null) {
            requestBody.add("client_id", keycloakConfig.getKeycloakClientId());
        }
        if (keycloakConfig.getKeycloakClientSecret() != null) {
            requestBody.add("client_secret", keycloakConfig.getKeycloakClientSecret());
        }
        if (keycloakConfig.getScope() != null) {
            requestBody.add("scope", keycloakConfig.getScope());
        }
        if (grantType != null) {
            requestBody.add("grant_type", grantType);
        }
        if (username != null) {
            requestBody.add("username", username);
        }
        if (password != null) {
            requestBody.add("password", password);
        }
        if (refreshToken != null) {
            requestBody.add("refresh_token", refreshToken);
        }

        return new HttpEntity<>(requestBody, headers);
    }

    public KeycloakRole getRoleByName(String clientId, String token, String role) {
        // create http request for get role by name
        String url = String.format("%s/admin/realms/master/clients/%s/roles/%s", keycloakConfig.getKeycloakBaseURL(), clientId, role);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // call keycloak api to get role by name
        ResponseEntity<KeycloakRole> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                KeycloakRole.class
        );

        log.info("Get role by name response from Keycloak: {}", response);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get role from Keycloak: " + response.getBody());
        }
    }

    public KeycloakUserDTO fetchFirstUserByUsername(String username, String token) {
        String url = String.format("%s/admin/realms/master/users?username=%s", keycloakConfig.getKeycloakBaseURL(), username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                KeycloakUserDTO[].class
        );

        log.info("Fetch user by username response from Keycloak: {}", response);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            KeycloakUserDTO[] users =  response.getBody();
            if (users != null && users.length > 0) {
                return users[0];
            } else {
                throw new ResourceNotFoundException("User", "username", username);
            }

        } else {
            throw new RuntimeException("Failed to fetch user from Keycloak: " + response.getBody());
        }
    }

    public void assignRoleToUser(String userId, String clientId, List<KeycloakRole> roles, String token) {
        // keycloak api to assign role to user: POST /admin/realms/{realm}/users/{id}/role-mappings/clients/{clientId}
        String url = String.format("%s/admin/realms/master/users/%s/role-mappings/clients/%s", keycloakConfig.getKeycloakBaseURL(), userId, clientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<List<KeycloakRole>> requestEntity = new HttpEntity<>(roles, headers);

        try {
            // call keycloak api to assign role to user
            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );

            log.info("Assign role to user response from Keycloak: {}", response);
        } catch (Exception e) {
            log.error("Error while assigning role to user in Keycloak: {}", e.getMessage());
            throw new RuntimeException("Failed to assign role to user in Keycloak: " + e.getMessage());
        }
    }

    public KeycloakUserDTO fetchUserProfileByToken(String token) {
        String url = String.format("%s/realms/master/protocol/openid-connect/userinfo", keycloakConfig.getKeycloakBaseURL());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            // call keycloak api to assign role to user
            ResponseEntity<KeycloakUserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    KeycloakUserDTO.class
            );

            log.info("Fetch user profile by token response from Keycloak: {}", response);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error while fetching user profile by token in Keycloak: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch user profile by token in Keycloak: " + e.getMessage());
        }
    }
}
