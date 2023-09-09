package com.ivanrogulj.Backend.Repositories;

import com.ivanrogulj.Backend.Entities.Category;
import com.ivanrogulj.Backend.Entities.Post;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, Long> {
    Page<Post> getPostsByAuthorId(Long id, Pageable pageable);
    Page<Post>  findByCategory(Category category, Pageable pageable);
    Optional<Post> getPostsById(Long id);
    Page<Post> findByTitleContainingIgnoreCase(String titleQuery, Pageable pageable);
    Page<Post> findAll(Pageable pageable);
}
