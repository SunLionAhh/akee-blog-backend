package com.akee.blog.controller;

import com.akee.blog.dto.PostDTO;
import com.akee.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "文章管理", description = "文章相关的 API 接口")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @Operation(summary = "创建文章", description = "创建一篇新的文章")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.createPost(postDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章", description = "根据文章 ID 更新文章信息")
    public ResponseEntity<PostDTO> updatePost(
            @Parameter(description = "文章 ID") @PathVariable Long id,
            @RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.updatePost(id, postDTO));
    }

    @DeleteMapping("/{id}")
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
    public ResponseEntity<Page<PostDTO>> getAllPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "获取分类文章", description = "根据分类 ID 获取文章列表")
    public ResponseEntity<Page<PostDTO>> getPostsByCategory(
            @Parameter(description = "分类 ID") @PathVariable Long categoryId,
            Pageable pageable) {
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId, pageable));
    }

    @GetMapping("/tag/{tagId}")
    @Operation(summary = "获取标签文章", description = "根据标签 ID 获取文章列表")
    public ResponseEntity<Page<PostDTO>> getPostsByTag(
            @Parameter(description = "标签 ID") @PathVariable Long tagId,
            Pageable pageable) {
        return ResponseEntity.ok(postService.getPostsByTag(tagId, pageable));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户文章", description = "根据用户 ID 获取文章列表")
    public ResponseEntity<Page<PostDTO>> getPostsByUser(
            @Parameter(description = "用户 ID") @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(postService.getPostsByUser(userId, pageable));
    }

    @PostMapping("/{id}/view")
    @Operation(summary = "增加浏览量", description = "增加文章的浏览量")
    public ResponseEntity<Void> incrementViewCount(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        postService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "增加点赞数", description = "增加文章的点赞数")
    public ResponseEntity<Void> incrementLikeCount(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        postService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "减少点赞数", description = "减少文章的点赞数")
    public ResponseEntity<Void> decrementLikeCount(
            @Parameter(description = "文章 ID") @PathVariable Long id) {
        postService.decrementLikeCount(id);
        return ResponseEntity.ok().build();
    }
} 