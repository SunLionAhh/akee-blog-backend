package com.akee.blog.repository;

import com.akee.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN p.category c WHERE c.id = :categoryId")
    Page<Post> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId")
    Page<Post> findByTagsId(@Param("tagId") Long tagId, Pageable pageable);

    Page<Post> findByUserId(Long userId, Pageable pageable);
} 