package com.akee.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TagDTO {
    private Long id;
    private String name;
    private Long postCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 