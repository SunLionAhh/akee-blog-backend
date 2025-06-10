package com.akee.blog.controller;

import com.akee.blog.dto.TagDTO;
import com.akee.blog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "标签管理", description = "标签相关的 API 接口")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    @Operation(summary = "创建标签", description = "创建一个新的标签")
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        return ResponseEntity.ok(tagService.createTag(tagDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新标签", description = "根据标签 ID 更新标签信息")
    public ResponseEntity<TagDTO> updateTag(
            @Parameter(description = "标签 ID") @PathVariable Long id,
            @RequestBody TagDTO tagDTO) {
        return ResponseEntity.ok(tagService.updateTag(id, tagDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签", description = "根据标签 ID 删除标签")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "标签 ID") @PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取标签", description = "根据标签 ID 获取标签信息")
    public ResponseEntity<TagDTO> getTagById(
            @Parameter(description = "标签 ID") @PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @GetMapping
    @Operation(summary = "获取所有标签", description = "分页获取所有标签信息")
    public ResponseEntity<Page<TagDTO>> getAllTags(Pageable pageable) {
        return ResponseEntity.ok(tagService.getAllTags(pageable));
    }
} 