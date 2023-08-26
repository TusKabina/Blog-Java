package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
