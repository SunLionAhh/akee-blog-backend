package com.akee.blog.controller;

import com.akee.blog.dto.CommentDTO;
import com.akee.blog.entity.Comment;
import com.akee.blog.service.CommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "评论管理", description = "评论相关的 API 接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "创建评论", description = "创建一个新的评论")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.createComment(commentDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "更新评论", description = "根据评论 ID 更新评论信息")
    public ResponseEntity<CommentDTO> updateComment(
            @Parameter(description = "评论 ID") @PathVariable Long id,
            @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "删除评论", description = "根据评论 ID 删除评论")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "评论 ID") @PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取评论", description = "根据评论 ID 获取评论信息")
    public ResponseEntity<CommentDTO> getCommentById(
            @Parameter(description = "评论 ID") @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping
    @Operation(summary = "获取所有评论", description = "分页获取所有评论信息")
    public ResponseEntity<IPage<CommentDTO>> getAllComments(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> page = new Page<>(current, size);
        return ResponseEntity.ok(commentService.getAllComments(page));
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "获取文章评论", description = "根据文章 ID 获取评论列表")
    public ResponseEntity<IPage<CommentDTO>> getCommentsByPost(
            @Parameter(description = "文章 ID") @PathVariable Long postId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> page = new Page<>(current, size);
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, page));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户评论", description = "根据用户 ID 获取评论列表")
    public ResponseEntity<IPage<CommentDTO>> getCommentsByUser(
            @Parameter(description = "用户 ID") @PathVariable Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> page = new Page<>(current, size);
        return ResponseEntity.ok(commentService.getCommentsByUser(userId, page));
    }

    @GetMapping("/{commentId}/replies")
    @Operation(summary = "获取评论回复", description = "根据评论 ID 获取回复列表")
    public ResponseEntity<IPage<CommentDTO>> getRepliesByComment(
            @Parameter(description = "评论 ID") @PathVariable Long commentId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> page = new Page<>(current, size);
        return ResponseEntity.ok(commentService.getRepliesByComment(commentId, page));
    }
} 