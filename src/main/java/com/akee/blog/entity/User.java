package com.akee.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;

    @TableField("avatar")
    private String avatar;

    @TableField("bio")
    private String bio;

    @TableField("status")
    private String status;

    @TableField("reset_password_token")
    private String resetPasswordToken;

    @TableField("reset_password_expires")
    private LocalDateTime resetPasswordExpires;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 