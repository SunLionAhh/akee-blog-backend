package com.akee.blog.service;

import com.akee.blog.dto.CommentDTO;
import com.akee.blog.entity.Comment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentService extends IService<Comment> {
    IPage<CommentDTO> getAllComments(Page<Comment> page);
    IPage<CommentDTO> getCommentsByPost(Long postId, Page<Comment> page);
    IPage<CommentDTO> getCommentsByUser(Long userId, Page<Comment> page);
    IPage<CommentDTO> getRepliesByComment(Long commentId, Page<Comment> page);
    CommentDTO getCommentById(Long id);
    CommentDTO createComment(CommentDTO commentDTO);
    CommentDTO updateComment(Long id, CommentDTO commentDTO);
    void deleteComment(Long id);
    List<CommentDTO> getRepliesByCommentId(Long commentId);
} 