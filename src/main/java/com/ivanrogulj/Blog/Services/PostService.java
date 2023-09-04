package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Entities.Category;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.Repositories.CategoryRepository;
import com.ivanrogulj.Blog.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public PostDTO getPostDtoById(Long id)
    {
        Post post = postRepository.findById(id).orElse(null);

        if(post == null)
        {
            return null;
        }
        return entityToDtoMapper.convertToPostDto(post);
    }

    public List<PostDTO> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        List<Post> posts = postRepository.findByCategory(category);
        return posts.stream()
                .map(entityToDtoMapper::convertToPostDto)
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostByUser(Long id) {
        List<Post> posts = postRepository.getPostsByAuthorId(id);
        return posts.stream()
                .map(entityToDtoMapper::convertToPostDto)
                .collect(Collectors.toList());

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

    public Post convertToPostEntity(PostDTO postDTO) {
        Post post = new Post();

        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());

        return post;
    }



    public PostDTO assignCategoryToPost(Long postId, Long categoryId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if (postOptional.isPresent() && categoryOptional.isPresent()) {
            Post post = postOptional.get();
            Category category = categoryOptional.get();

            post.setCategory(category);
            postRepository.save(post);

            return entityToDtoMapper.convertToPostDto(post);
        }

        return null;
    }
}