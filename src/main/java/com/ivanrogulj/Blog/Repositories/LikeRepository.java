package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
