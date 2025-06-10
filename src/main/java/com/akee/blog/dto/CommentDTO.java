package com.akee.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private Long postId;
    private Long parentId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 