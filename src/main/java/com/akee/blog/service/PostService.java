package com.akee.blog.service;

import com.akee.blog.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    PostDTO updatePost(Long id, PostDTO postDTO);
    void deletePost(Long id);
    PostDTO getPostById(Long id);
    Page<PostDTO> getAllPosts(Pageable pageable);
    Page<PostDTO> getPostsByCategory(Long categoryId, Pageable pageable);
    Page<PostDTO> getPostsByTag(Long tagId, Pageable pageable);
    Page<PostDTO> getPostsByUser(Long userId, Pageable pageable);
    void incrementViewCount(Long id);
    void incrementLikeCount(Long id);
    void decrementLikeCount(Long id);
} 