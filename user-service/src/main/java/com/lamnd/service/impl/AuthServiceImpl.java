package com.lamnd.service.impl;

import com.lamnd.dto.identity.TokenResponse;
import com.lamnd.dto.request.RegistrationRequest;
import com.lamnd.dto.response.AuthResponse;
import com.lamnd.entity.User;
import com.lamnd.repository.UserRepo;
import com.lamnd.service.AuthService;
import com.lamnd.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final KeycloakService keycloakService;

    @Override
    public AuthResponse login(String username, String password) {
        // get access token from keycloak
        TokenResponse tokenResponse = keycloakService.getAccessToken(username,
                password, "password", null);

        // map token response to auth response
        return AuthResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .message("Login successfully")
                .build();
    }

    @Override
    public AuthResponse signup(RegistrationRequest registrationRequest) {
        // call keycloak service to create user in keycloak
        keycloakService.register(registrationRequest);

        // create user in my own system
        User user = User.builder()
                .username(registrationRequest.username())
                .password(registrationRequest.password())
                .fullName(registrationRequest.firstName() + " " + registrationRequest.lastName())
                .role(registrationRequest.role())
                .email(registrationRequest.email())
                .build();

        userRepo.save(user);

        // get access token from keycloak
        TokenResponse tokenResponse = keycloakService.getAccessToken(registrationRequest.username(),
                registrationRequest.password(), "password", null);

        // map token response to auth response
        return AuthResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .message("User registered successfully")
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        // get access token from keycloak
        TokenResponse tokenResponse = keycloakService.getAccessToken(null,
                null, "refresh_token", refreshToken);

        // map token response to auth response
        return AuthResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .message("Token refreshed successfully")
                .build();
    }
}
