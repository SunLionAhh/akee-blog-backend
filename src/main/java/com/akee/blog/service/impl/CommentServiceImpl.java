package com.akee.blog.service.impl;

import com.akee.blog.dto.CommentDTO;
import com.akee.blog.entity.Comment;
import com.akee.blog.mapper.CommentMapper;
import com.akee.blog.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public IPage<CommentDTO> getAllComments(Page<Comment> page) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            return commentDTO;
        });
    }

    @Override
    public IPage<CommentDTO> getCommentsByPost(Long postId, Page<Comment> page) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId)
               .orderByDesc(Comment::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            return commentDTO;
        });
    }

    @Override
    public IPage<CommentDTO> getCommentsByUser(Long userId, Page<Comment> page) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId)
               .orderByDesc(Comment::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            return commentDTO;
        });
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        Comment comment = baseMapper.selectById(id);
        if (comment == null) {
            return null;
        }
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);
        return commentDTO;
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        baseMapper.insert(comment);
        commentDTO.setId(comment.getId());
        return commentDTO;
    }

    @Override
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = baseMapper.selectById(id);
        if (comment == null) {
            return null;
        }
        BeanUtils.copyProperties(commentDTO, comment);
        baseMapper.updateById(comment);
        return commentDTO;
    }

    @Override
    public void deleteComment(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<CommentDTO> getRepliesByCommentId(Long commentId) {
        List<Comment> replies = baseMapper.selectList(
            new LambdaQueryWrapper<Comment>()
                .eq(Comment::getParentId, commentId)
        );
        return replies.stream()
            .map(reply -> {
                CommentDTO replyDTO = new CommentDTO();
                BeanUtils.copyProperties(reply, replyDTO);
                if (reply.getUser() != null) {
                    replyDTO.setUsername(reply.getUser().getUsername());
                    replyDTO.setUserAvatar(reply.getUser().getAvatar());
                }
                return replyDTO;
            })
            .collect(Collectors.toList());
    }

    @Override
    public IPage<CommentDTO> getRepliesByComment(Long commentId, Page<Comment> page) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId, commentId)
               .orderByDesc(Comment::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            return commentDTO;
        });
    }
} 