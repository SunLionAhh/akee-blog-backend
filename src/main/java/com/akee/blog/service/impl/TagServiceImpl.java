package com.akee.blog.service.impl;

import com.akee.blog.dto.TagDTO;
import com.akee.blog.entity.Tag;
import com.akee.blog.mapper.TagMapper;
import com.akee.blog.mapper.PostMapper;
import com.akee.blog.service.TagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final PostMapper postMapper;

    public TagServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public List<TagDTO> getAllTags() {
        List<Tag> tags = baseMapper.selectList(null);
        return tags.stream()
            .map(tag -> {
                TagDTO tagDTO = new TagDTO();
                BeanUtils.copyProperties(tag, tagDTO);
                tagDTO.setPostCount(postMapper.selectCount(
                    new LambdaQueryWrapper<com.akee.blog.entity.Post>()
                        .inSql(com.akee.blog.entity.Post::getId, 
                            "SELECT post_id FROM post_tags WHERE tag_id = " + tag.getId())
                ));
                return tagDTO;
            })
            .collect(Collectors.toList());
    }

    @Override
    public IPage<TagDTO> getAllTags(Page<Tag> page) {
        IPage<Tag> tagPage = baseMapper.selectPage(page, null);
        return tagPage.convert(tag -> {
            TagDTO tagDTO = new TagDTO();
            BeanUtils.copyProperties(tag, tagDTO);
            tagDTO.setPostCount(postMapper.selectCount(
                new LambdaQueryWrapper<com.akee.blog.entity.Post>()
                    .inSql(com.akee.blog.entity.Post::getId, 
                        "SELECT post_id FROM post_tags WHERE tag_id = " + tag.getId())
            ));
            return tagDTO;
        });
    }

    @Override
    public TagDTO getTagById(Long id) {
        Tag tag = baseMapper.selectById(id);
        if (tag == null) {
            return null;
        }
        TagDTO tagDTO = new TagDTO();
        BeanUtils.copyProperties(tag, tagDTO);
        tagDTO.setPostCount(postMapper.selectCount(
            new LambdaQueryWrapper<com.akee.blog.entity.Post>()
                .inSql(com.akee.blog.entity.Post::getId, 
                    "SELECT post_id FROM post_tags WHERE tag_id = " + id)
        ));
        return tagDTO;
    }

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagDTO, tag);
        baseMapper.insert(tag);
        tagDTO.setId(tag.getId());
        return tagDTO;
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag tag = baseMapper.selectById(id);
        if (tag == null) {
            return null;
        }
        BeanUtils.copyProperties(tagDTO, tag);
        baseMapper.updateById(tag);
        return tagDTO;
    }

    @Override
    public void deleteTag(Long id) {
        baseMapper.deleteById(id);
    }
} 