package com.akee.blog.service;

import com.akee.blog.dto.PostDTO;
import com.akee.blog.entity.Post;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PostService extends IService<Post> {
    IPage<PostDTO> getAllPosts(Page<Post> page);
    PostDTO getPostById(Long id);
    PostDTO createPost(PostDTO postDTO);
    PostDTO updatePost(Long id, PostDTO postDTO);
    void deletePost(Long id);
    IPage<PostDTO> getPostsByCategory(Long categoryId, Page<Post> page);
    IPage<PostDTO> getPostsByTag(Long tagId, Page<Post> page);
    IPage<PostDTO> getPostsByUser(Long userId, Page<Post> page);
    void incrementViewCount(Long id);
    void incrementLikeCount(Long id);
    void decrementLikeCount(Long id);
} 