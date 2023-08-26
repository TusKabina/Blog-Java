package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.Comment;
import com.ivanrogulj.Blog.Entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserId(Long userId);
}
