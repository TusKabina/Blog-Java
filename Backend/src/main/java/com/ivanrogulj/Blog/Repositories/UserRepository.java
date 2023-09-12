package com.ivanrogulj.Blog.Repositories;

import com.ivanrogulj.Blog.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsernameContainingIgnoreCase(String query);
    Page<User> findByFullNameContainingIgnoreCase(String fullNameQuery, Pageable pageable);

    Page<User> findAll(Pageable pageable);
}
