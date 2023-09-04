package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository  extends JpaRepository<Category, Long> {

}

