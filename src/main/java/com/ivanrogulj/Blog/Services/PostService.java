package com.ivanrogulj.Blog.Services;


import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.Post;

import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.ExceptionHandler.DataNotFoundException;
import com.ivanrogulj.Blog.ExceptionHandler.ForbiddenException;
import com.ivanrogulj.Blog.Repositories.CategoryRepository;
import com.ivanrogulj.Blog.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    public Page<PostDTO> getAllPosts(Pageable pageable) {
        Page<Post> postPage =  postRepository.findAll(pageable);
        return postPage.map(entityToDtoMapper::convertToPostDto);
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

    public Page<PostDTO> getPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Category not found!"));
       Page<Post> postPage = postRepository.findByCategory(category, pageable);
        return postPage.map(entityToDtoMapper::convertToPostDto);
    }

    public Page<PostDTO> getPostsByUser(Long id, Pageable pageable) {
        Page<Post> postPage = postRepository.getPostsByAuthorId(id, pageable);

        return postPage.map(entityToDtoMapper::convertToPostDto);

    }

    public PostDTO createPost(PostDTO postDTO, Long authorId) {
        if(!isAuthorizedToCreate(authorId))
        {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
        UserDTO authorDto = userService.getUserById(authorId);

        postDTO.setAuthor(authorDto);
        postDTO.setCreationDate(LocalDateTime.now());
        Post post = entityToDtoMapper.convertDtoToPost(postDTO);
        postRepository.save(post);
        postDTO.setId(post.getId());
        return postDTO;
    }

    public PostDTO updatePost(Long id, PostDTO postDTO) {
        if(!isAuthorized(id))
        {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
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
        if(!isAuthorized(id))
        {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
        postRepository.deleteById(id);
    }


    public PostDTO assignCategoryToPost(Long postId, Long categoryId) {
        if(!isAuthorized(postId))
        {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
        Post post = postRepository.findById(postId).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new DataNotFoundException("Category not found!"));
        post.setCategory(category);

        return entityToDtoMapper.convertToPostDto(post);

    }


    private boolean isAuthorized(Long id) {
        PostDTO postDto = getPostById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        return postDto.getAuthor().getUsername().equals(authentication.getName()) || isAdmin;
    }

    private boolean isAuthorizedToCreate(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        return Objects.equals(userDetails.getUsername(), userService.getUserById(id).getUsername()) || isAdmin;
    }

}