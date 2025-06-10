package com.akee.blog.controller;

import com.akee.blog.dto.CommentDTO;
import com.akee.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "评论管理", description = "评论相关的 API 接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @Operation(summary = "创建评论", description = "创建一条新的评论")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.createComment(commentDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新评论", description = "根据评论 ID 更新评论信息")
    public ResponseEntity<CommentDTO> updateComment(
            @Parameter(description = "评论 ID") @PathVariable Long id,
            @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDTO));
    }

    @DeleteMapping("/{id}")
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
    public ResponseEntity<Page<CommentDTO>> getAllComments(Pageable pageable) {
        return ResponseEntity.ok(commentService.getAllComments(pageable));
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "获取文章评论", description = "根据文章 ID 获取评论列表")
    public ResponseEntity<Page<CommentDTO>> getCommentsByPost(
            @Parameter(description = "文章 ID") @PathVariable Long postId,
            Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, pageable));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户评论", description = "根据用户 ID 获取评论列表")
    public ResponseEntity<Page<CommentDTO>> getCommentsByUser(
            @Parameter(description = "用户 ID") @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByUser(userId, pageable));
    }

    @GetMapping("/{commentId}/replies")
    @Operation(summary = "获取评论回复", description = "根据评论 ID 获取回复列表")
    public ResponseEntity<Page<CommentDTO>> getRepliesByComment(
            @Parameter(description = "评论 ID") @PathVariable Long commentId,
            Pageable pageable) {
        return ResponseEntity.ok(commentService.getRepliesByComment(commentId, pageable));
    }
} 