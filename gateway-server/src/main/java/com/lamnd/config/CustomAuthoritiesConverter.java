package com.lamnd.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        String REALM_ACCESS = "realm_access";
        String RESOURCE_ACCESS = "resource_access";

        Map<String, Object> realmAccessMap = source.getClaimAsMap(REALM_ACCESS);

        if (realmAccessMap != null && realmAccessMap.containsKey("roles")) {
            Object roles = realmAccessMap.get("roles");

            if (roles instanceof List<?> stringRoles) {
                 stringRoles.forEach(role -> authorities.add(
                         new SimpleGrantedAuthority(String.format("%s%s", ROLE_PREFIX, role))));
            }
        }

        Map<String, Object> resourceAccessMap = source.getClaimAsMap(RESOURCE_ACCESS);

        if (resourceAccessMap != null) {
            resourceAccessMap.forEach((client, clientDetails) -> {
                Map<String, Object> clientRoles = (Map<String, Object>) clientDetails;

                if (clientRoles.containsKey("roles")) {
                    List<String> roles = (List<String>) clientRoles.get("roles");
                    authorities.addAll(roles.stream()
                            .map(role -> new SimpleGrantedAuthority(String.format("%s%s", ROLE_PREFIX, role)))
                            .toList());
                }
            });
        }

        return authorities;
    }
}
