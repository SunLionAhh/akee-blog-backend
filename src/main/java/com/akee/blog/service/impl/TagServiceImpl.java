package com.akee.blog.service.impl;

import com.akee.blog.dto.TagDTO;
import com.akee.blog.entity.Tag;
import com.akee.blog.repository.TagRepository;
import com.akee.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagDTO, tag);
        tag = tagRepository.save(tag);
        BeanUtils.copyProperties(tag, tagDTO);
        return tagDTO;
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        BeanUtils.copyProperties(tagDTO, tag);
        tag = tagRepository.save(tag);
        BeanUtils.copyProperties(tag, tagDTO);
        return tagDTO;
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        TagDTO tagDTO = new TagDTO();
        BeanUtils.copyProperties(tag, tagDTO);
        return tagDTO;
    }

    @Override
    public Page<TagDTO> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(tag -> {
                    TagDTO tagDTO = new TagDTO();
                    BeanUtils.copyProperties(tag, tagDTO);
                    return tagDTO;
                });
    }
} 