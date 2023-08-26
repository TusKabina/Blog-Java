package com.ivanrogulj.Blog.Services;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ivanrogulj.Blog.Repositories.CommentRepository;
import com.ivanrogulj.Blog.Entities.Comment;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId);
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