package com.akee.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long postCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 