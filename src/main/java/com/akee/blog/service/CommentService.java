package com.akee.blog.service;

import com.akee.blog.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);
    CommentDTO updateComment(Long id, CommentDTO commentDTO);
    void deleteComment(Long id);
    CommentDTO getCommentById(Long id);
    Page<CommentDTO> getAllComments(Pageable pageable);
    Page<CommentDTO> getCommentsByPost(Long postId, Pageable pageable);
    Page<CommentDTO> getCommentsByUser(Long userId, Pageable pageable);
    Page<CommentDTO> getRepliesByComment(Long commentId, Pageable pageable);
} 