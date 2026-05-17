package com.lamnd.service;

import com.lamnd.dto.request.RegistrationRequest;
import com.lamnd.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username, String password);

    AuthResponse signup(RegistrationRequest registrationRequest);

    AuthResponse refreshToken(String refreshToken);
}
