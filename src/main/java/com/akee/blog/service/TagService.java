package com.akee.blog.service;

import com.akee.blog.dto.TagDTO;
import com.akee.blog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<TagDTO> getAllTags();
    IPage<TagDTO> getAllTags(Page<Tag> page);
    TagDTO getTagById(Long id);
    TagDTO createTag(TagDTO tagDTO);
    TagDTO updateTag(Long id, TagDTO tagDTO);
    void deleteTag(Long id);
} 