package com.akee.blog.service;

import com.akee.blog.dto.TagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {
    TagDTO createTag(TagDTO tagDTO);
    TagDTO updateTag(Long id, TagDTO tagDTO);
    void deleteTag(Long id);
    TagDTO getTagById(Long id);
    Page<TagDTO> getAllTags(Pageable pageable);
} 