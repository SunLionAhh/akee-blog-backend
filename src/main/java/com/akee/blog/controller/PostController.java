package com.akee.blog.controller;

import com.akee.blog.dto.PostDTO;
import com.akee.blog.entity.Post;
import com.akee.blog.entity.PostStatus;
import com.akee.blog.mapper.UserMapper;
import com.akee.blog.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "文章管理", description = "文章的增删改查接口")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "创建文章", description = "创建一篇新的文章")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.createPost(postDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "更新文章", description = "根据文章 ID 更新文章信息")
    public ResponseEntity<PostDTO> updatePost(
            @Parameter(description = "文章 ID") @PathVariable Long id,
            @RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.updatePost(id, postDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "删除文章", description = "根据文章 ID 删除文章")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章", description = "根据文章 ID 获取文章信息")
    public ResponseEntity<PostDTO> getPostById(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    @Operation(summary = "获取所有文章", description = "分页获取所有文章信息")
    public ResponseEntity<IPage<PostDTO>> getAllPosts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Post> page = new Page<>(current, size);
        return ResponseEntity.ok(postService.getAllPosts(page));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "获取分类文章", description = "根据分类 ID 获取文章列表")
    public ResponseEntity<IPage<PostDTO>> getPostsByCategory(
            @Parameter(description = "分类 ID") @PathVariable Long categoryId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Post> page = new Page<>(current, size);
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId, page));
    }

    @GetMapping("/tag/{tagId}")
    @Operation(summary = "获取标签文章", description = "根据标签 ID 获取文章列表")
    public ResponseEntity<IPage<PostDTO>> getPostsByTag(
            @Parameter(description = "标签 ID") @PathVariable Long tagId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Post> page = new Page<>(current, size);
        return ResponseEntity.ok(postService.getPostsByTag(tagId, page));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户文章", description = "根据用户 ID 获取文章列表")
    public ResponseEntity<IPage<PostDTO>> getPostsByUser(
            @Parameter(description = "用户 ID") @PathVariable Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Post> page = new Page<>(current, size);
        return ResponseEntity.ok(postService.getPostsByUser(userId, page));
    }

    @PostMapping("/{id}/view")
    @Operation(summary = "增加浏览量", description = "增加文章的浏览量")
    public ResponseEntity<Void> incrementViewCount(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        postService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "增加点赞数", description = "增加文章的点赞数")
    public ResponseEntity<Void> incrementLikeCount(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        postService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "减少点赞数", description = "减少文章的点赞数")
    public ResponseEntity<Void> decrementLikeCount(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        postService.decrementLikeCount(id);
        return ResponseEntity.ok().build();
    }
} 