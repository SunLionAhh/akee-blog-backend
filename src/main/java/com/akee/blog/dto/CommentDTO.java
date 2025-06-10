package com.akee.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private String username;
    private String userAvatar;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 