package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPostId(Long postId);
}