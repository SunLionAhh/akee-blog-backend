package com.akee.blog.service.impl;

import com.akee.blog.dto.CategoryDTO;
import com.akee.blog.dto.PostDTO;
import com.akee.blog.dto.TagDTO;
import com.akee.blog.dto.UserDTO;
import com.akee.blog.entity.Post;
import com.akee.blog.entity.Category;
import com.akee.blog.entity.Tag;
import com.akee.blog.entity.User;
import com.akee.blog.mapper.PostMapper;
import com.akee.blog.mapper.CategoryMapper;
import com.akee.blog.mapper.TagMapper;
import com.akee.blog.mapper.UserMapper;
import com.akee.blog.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@CacheConfig(cacheNames = "posts")
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @CacheEvict(allEntries = true)
    public PostDTO createPost(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        baseMapper.insert(post);
        postDTO.setId(post.getId());
        return postDTO;
    }

    @Override
    @CacheEvict(allEntries = true)
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post post = baseMapper.selectById(id);
        if (post == null) {
            return null;
        }
        BeanUtils.copyProperties(postDTO, post);
        baseMapper.updateById(post);
        return postDTO;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deletePost(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public IPage<PostDTO> getPostsByCategory(Long categoryId, Page<Post> page) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getCategoryId, categoryId)
               .orderByDesc(Post::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(post -> {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post, postDTO);
            return postDTO;
        });
    }

    @Override
    public IPage<PostDTO> getPostsByTag(Long tagId, Page<Post> page) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.inSql(Post::getId, "SELECT post_id FROM post_tags WHERE tag_id = " + tagId)
               .orderByDesc(Post::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(post -> {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post, postDTO);
            return postDTO;
        });
    }

    @Override
    public IPage<PostDTO> getPostsByUser(Long userId, Page<Post> page) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getUserId, userId)
               .orderByDesc(Post::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(post -> {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post, postDTO);
            return postDTO;
        });
    }

    @Override
    public IPage<PostDTO> getAllPosts(Page<Post> page) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Post::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(post -> {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post, postDTO);
            
            // 设置分类信息
            if (post.getCategory() != null) {
                postDTO.setCategoryName(post.getCategory().getName());
            }
            
            // 设置作者信息
            if (post.getUser() != null) {
                postDTO.setUsername(post.getUser().getUsername());
                postDTO.setUserAvatar(post.getUser().getAvatar());
            }
            
            // 设置标签信息
            if (post.getTags() != null) {
                postDTO.setTagIds(post.getTags().stream()
                    .map(tag -> tag.getId())
                    .collect(Collectors.toSet()));
                postDTO.setTagNames(post.getTags().stream()
                    .map(tag -> tag.getName())
                    .collect(Collectors.toSet()));
            }
            
            return postDTO;
        });
    }

    @Override
    @Cacheable(key = "#id")
    public PostDTO getPostById(Long id) {
        Post post = baseMapper.selectById(id);
        if (post == null) {
            return null;
        }
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post, postDTO);
        return postDTO;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void incrementViewCount(Long id) {
        Post post = baseMapper.selectById(id);
        if (post != null) {
            post.setViewCount(post.getViewCount() + 1);
            baseMapper.updateById(post);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public void incrementLikeCount(Long id) {
        Post post = baseMapper.selectById(id);
        if (post != null) {
            post.setLikeCount(post.getLikeCount() + 1);
            baseMapper.updateById(post);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public void decrementLikeCount(Long id) {
        Post post = baseMapper.selectById(id);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        baseMapper.updateById(post);
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post, postDTO);

        // 获取分类信息
        Category category = categoryMapper.selectById(post.getCategoryId());
        if (category != null) {
            postDTO.setCategoryName(category.getName());
        }

        // 获取用户信息
        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            postDTO.setUsername(user.getUsername());
            postDTO.setUserAvatar(user.getAvatar());
        }

        // 获取标签信息
        Set<Long> tagIds = new HashSet<>();
        Set<String> tagNames = new HashSet<>();
        // TODO: 实现标签查询逻辑
        postDTO.setTagIds(tagIds);
        postDTO.setTagNames(tagNames);

        return postDTO;
    }
} 