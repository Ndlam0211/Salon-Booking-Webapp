package com.lamnd.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        String REALM_ACCESS = "realm_access";
        String RESOURCE_ACCESS = "resource_access";
        Map<String, Object> realmAccessMap = source.getClaimAsMap(REALM_ACCESS);

        if (realmAccessMap != null && realmAccessMap.containsKey("roles")) {
            Object roles = realmAccessMap.get("roles");

            if (roles instanceof List<?> stringRoles) {
                return ((List<?>) stringRoles)
                        .stream()
                                .map(role -> new SimpleGrantedAuthority(String.format("%s%s", ROLE_PREFIX, role)))
                                .collect(Collectors.toList());
            }
        }

        return List.of();
    }
}
