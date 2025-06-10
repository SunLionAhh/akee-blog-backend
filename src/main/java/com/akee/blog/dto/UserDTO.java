package com.akee.blog.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String avatar;
    private String bio;
    private String status;
} 