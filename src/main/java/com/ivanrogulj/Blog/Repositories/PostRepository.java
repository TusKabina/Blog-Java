package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository  extends JpaRepository<Post, Long> {
}
