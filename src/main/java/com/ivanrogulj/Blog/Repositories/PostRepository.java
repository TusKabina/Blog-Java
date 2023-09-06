package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> getPostsByAuthorId(Long userId);
    List<Post> findByCategory(Category category);
    Optional<Post> getPostsById(Long id);
}
