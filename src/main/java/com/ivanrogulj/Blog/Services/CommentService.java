package com.ivanrogulj.Blog.Services;
import com.ivanrogulj.Blog.DTO.CommentDTO;
import com.ivanrogulj.Blog.Entities.Post;
import com.ivanrogulj.Blog.Entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ivanrogulj.Blog.Repositories.CommentRepository;
import com.ivanrogulj.Blog.Entities.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final EntityToDtoMapper entityToDtoMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService, EntityToDtoMapper entityToDtoMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    public Comment createComment(Comment comment, Long userId, Long postId) {
        User user = userService.getUserById(userId).orElse(null);
        Post post = postService.getPostById(postId);

        if (user != null && post != null) {
            comment.setUser(user);
            comment.setPost(post);
            comment.setCreationDate(LocalDateTime.now());
            return commentRepository.save(comment);
        }

        return null;
    }

    public List<CommentDTO> getCommentsForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(entityToDtoMapper::convertToCommentDto)
                .toList();
    }

    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {
        if (commentRepository.existsById(commentId)) {
            updatedComment.setId(commentId);
            return commentRepository.save(updatedComment);
        }
        throw new EntityNotFoundException("Comment not found");
    }

    public void deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new EntityNotFoundException("Comment not found");
        }
    }
}