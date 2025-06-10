package com.akee.blog.service.impl;

import com.akee.blog.dto.CategoryDTO;
import com.akee.blog.entity.Category;
import com.akee.blog.mapper.CategoryMapper;
import com.akee.blog.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public IPage<CategoryDTO> getAllCategories(Page<Category> page) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Category::getCreatedAt);
        return baseMapper.selectPage(page, wrapper).convert(category -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(category, categoryDTO);
            return categoryDTO;
        });
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = baseMapper.selectById(id);
        if (category == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        baseMapper.insert(category);
        categoryDTO.setId(category.getId());
        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = baseMapper.selectById(id);
        if (category == null) {
            return null;
        }
        BeanUtils.copyProperties(categoryDTO, category);
        baseMapper.updateById(category);
        return categoryDTO;
    }

    @Override
    public void deleteCategory(Long id) {
        baseMapper.deleteById(id);
    }
} 