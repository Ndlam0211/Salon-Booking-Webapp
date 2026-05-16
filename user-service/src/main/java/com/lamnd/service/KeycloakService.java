package com.lamnd.service;

import com.lamnd.config.KeycloakConfig;
import com.lamnd.dto.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakConfig keycloakConfig;
    private final RestTemplate restTemplate;

    public void register(RegistrationRequest request){

    }
}
