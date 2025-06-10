package com.akee.blog.service.impl;

import com.akee.blog.dto.PostDTO;
import com.akee.blog.entity.Post;
import com.akee.blog.entity.Category;
import com.akee.blog.entity.Tag;
import com.akee.blog.entity.User;
import com.akee.blog.repository.PostRepository;
import com.akee.blog.repository.CategoryRepository;
import com.akee.blog.repository.TagRepository;
import com.akee.blog.repository.UserRepository;
import com.akee.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setSummary(postDTO.getSummary());
        post.setStatus(postDTO.getStatus());
        post.setViewCount(0);
        post.setLikeCount(0);

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        post.setCategory(category);

        List<Tag> tags = new ArrayList<>();
        for (Long tagId : postDTO.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            tags.add(tag);
        }
        post.setTags(tags);

        User user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    @Override
    @Transactional
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setSummary(postDTO.getSummary());
        post.setStatus(postDTO.getStatus());

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        post.setCategory(category);

        List<Tag> tags = new ArrayList<>();
        for (Long tagId : postDTO.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            tags.add(tag);
        }
        post.setTags(tags);

        Post updatedPost = postRepository.save(post);
        return convertToDTO(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return convertToDTO(post);
    }

    @Override
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        // 创建一个新的 Pageable 对象，并添加按 createdAt 字段倒序排序
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return postRepository.findAll(sortedPageable).map(this::convertToDTO);
    }

    @Override
    public Page<PostDTO> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryId(categoryId, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<PostDTO> getPostsByTag(Long tagId, Pageable pageable) {
        return postRepository.findByTagsId(tagId, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<PostDTO> getPostsByUser(Long userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void incrementLikeCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void decrementLikeCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
        }
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setSummary(post.getSummary());
        postDTO.setCoverImage(post.getCover());
        postDTO.setStatus(post.getStatus());
        postDTO.setViewCount(post.getViewCount());
        postDTO.setLikeCount(post.getLikeCount());
        postDTO.setCommentCount(post.getCommentCount());
        postDTO.setCategoryId(post.getCategory().getId());
        postDTO.setCategoryName(post.getCategory().getName());
        postDTO.setUserId(post.getUser().getId());
        postDTO.setAuthorName(post.getUser().getUsername());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());

        List<Long> tagIds = new ArrayList<>();
        List<String> tagNames = new ArrayList<>();
        for (Tag tag : post.getTags()) {
            tagIds.add(tag.getId());
            tagNames.add(tag.getName());
        }
        postDTO.setTagIds(tagIds);
        postDTO.setTagNames(tagNames);

        return postDTO;
    }
} 