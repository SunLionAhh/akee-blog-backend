package com.akee.blog.dto;

import com.akee.blog.entity.PostStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private String status;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private String username;
    private String userAvatar;
    private Set<Long> tagIds;
    private Set<String> tagNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 