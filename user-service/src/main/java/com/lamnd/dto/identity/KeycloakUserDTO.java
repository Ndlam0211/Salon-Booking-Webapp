package com.lamnd.dto.identity;

import lombok.Data;

@Data
public class KeycloakUserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
