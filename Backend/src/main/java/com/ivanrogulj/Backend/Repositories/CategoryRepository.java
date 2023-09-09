package com.ivanrogulj.Backend.Repositories;

import com.ivanrogulj.Backend.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository  extends JpaRepository<Category, Long> {

}

