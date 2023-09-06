package com.ivanrogulj.Blog.Services;


import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.Post;

import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.ExceptionHandler.DataNotFoundException;
import com.ivanrogulj.Blog.Repositories.CategoryRepository;
import com.ivanrogulj.Blog.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    private final UserService userService;
    private final EntityToDtoMapper entityToDtoMapper;
    @Autowired
    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, CategoryService categoryService, UserService userService, EntityToDtoMapper entityToDtoMapper) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if(post == null) {
            throw new DataNotFoundException("Post not found!");
        }
        return entityToDtoMapper.convertToPostDto(post);
    }

    public PostDTO getPostDtoById(Long id)
    {
        Post post = postRepository.findById(id).orElse(null);

        if(post == null)
        {
            throw new DataNotFoundException("Post not found!");
        }
        return entityToDtoMapper.convertToPostDto(post);
    }

    public List<PostDTO> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category not found!"));
        List<Post> posts = postRepository.findByCategory(category).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        return posts.stream()
                .map(entityToDtoMapper::convertToPostDto)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostByUser(Long id) {
        List<Post> posts = postRepository.getPostsByAuthorId(id).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        return posts.stream()
                .map(entityToDtoMapper::convertToPostDto)
                .collect(Collectors.toList());

    }

    public PostDTO createPost(PostDTO postDTO, Long authorId) {
        UserDTO authorDto = userService.getUserById(authorId);

        postDTO.setAuthor(authorDto);
        postDTO.setCreationDate(LocalDateTime.now());
        Post post = entityToDtoMapper.convertDtoToPost(postDTO);
        postRepository.save(post);
        return postDTO;
    }

    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.getPostsById(id).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        if(postDTO.getContent() != null) {
            post.setContent(postDTO.getContent());
        }
        if(postDTO.getTitle() != null)
        {
            post.setTitle(postDTO.getTitle());
        }
        postRepository.save(post);

        return entityToDtoMapper.convertToPostDto(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }


    public PostDTO assignCategoryToPost(Long postId, Long categoryId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new DataNotFoundException("Category not found!"));
        post.setCategory(category);

        return entityToDtoMapper.convertToPostDto(post);

    }
}