package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
}
