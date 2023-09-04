package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.Repositories.CategoryRepository;
import com.ivanrogulj.Blog.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final EntityToDtoMapper entityToDtoMapper;
    @Autowired
    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, UserService userService, EntityToDtoMapper entityToDtoMapper) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<PostDTO> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        List<Post> posts = postRepository.findByCategory(category);
        return posts.stream()
                .map(entityToDtoMapper::convertToPostDto)
                .collect(Collectors.toList());
    }

    public List<Post> getPostByUser(Long id) {
        return postRepository.getPostsByAuthorId(id);
    }

    public Post createPost(Post post, Long authorId) {
        User author = userService.getUserById(authorId).orElse(null);
        if (author != null) {
            post.setAuthor(author);
            post.setCreationDate(LocalDateTime.now());
            return postRepository.save(post);
        }
        return null;
    }

    public Post updatePost(Long id, Post post) {
        if (postRepository.existsById(id)) {
            post.setId(id);
            return postRepository.save(post);
        }
        return null;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}