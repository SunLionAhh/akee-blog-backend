package com.akee.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String[] roles;

    public JwtResponse(String token, Long id, String username, String[] roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
} 