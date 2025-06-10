package com.akee.blog.service.impl;

import com.akee.blog.dto.CommentDTO;
import com.akee.blog.entity.Comment;
import com.akee.blog.repository.CommentRepository;
import com.akee.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment = commentRepository.save(comment);
        BeanUtils.copyProperties(comment, commentDTO);
        return commentDTO;
    }

    @Override
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        BeanUtils.copyProperties(commentDTO, comment, "id", "userId", "postId", "parentId", "createTime");
        comment = commentRepository.save(comment);
        BeanUtils.copyProperties(comment, commentDTO);
        return commentDTO;
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);
        return commentDTO;
    }

    @Override
    public Page<CommentDTO> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    return commentDTO;
                });
    }

    @Override
    public Page<CommentDTO> getCommentsByPost(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable)
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    return commentDTO;
                });
    }

    @Override
    public Page<CommentDTO> getCommentsByUser(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable)
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    return commentDTO;
                });
    }

    @Override
    public Page<CommentDTO> getRepliesByComment(Long commentId, Pageable pageable) {
        return commentRepository.findByParentId(commentId, pageable)
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    return commentDTO;
                });
    }
} 