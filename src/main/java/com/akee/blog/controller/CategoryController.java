package com.akee.blog.controller;

import com.akee.blog.dto.CategoryDTO;
import com.akee.blog.entity.Category;
import com.akee.blog.service.CategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "分类管理", description = "分类相关的 API 接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取所有分类", description = "分页获取所有分类信息")
    public ResponseEntity<IPage<CategoryDTO>> getAllCategories(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<Category> page = new Page<>(current, size);
        return ResponseEntity.ok(categoryService.getAllCategories(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类", description = "根据分类 ID 获取分类信息")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @Parameter(description = "分类 ID") @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "创建分类", description = "创建一个新的分类")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "根据分类 ID 更新分类信息")
    public ResponseEntity<CategoryDTO> updateCategory(
            @Parameter(description = "分类 ID") @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "根据分类 ID 删除分类")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "分类 ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
} 