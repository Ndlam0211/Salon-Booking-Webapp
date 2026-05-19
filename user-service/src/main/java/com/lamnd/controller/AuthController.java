package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.request.LoginRequest;
import com.lamnd.dto.request.RegistrationRequest;
import com.lamnd.dto.response.AuthResponse;
import com.lamnd.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> signup(@Valid @RequestBody RegistrationRequest registrationRequest) {
        AuthResponse response = authService.signup(registrationRequest);
        return createSuccessResponse(response);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest.username(), loginRequest.password());
        return createSuccessResponse(response);
    }

    @GetMapping("/access-token/refresh/{refreshToken}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> refreshToken(@PathVariable("refreshToken") String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return createSuccessResponse(response);
    }
}
