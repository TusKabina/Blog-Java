package com.ivanrogulj.Backend.Services;
import com.ivanrogulj.Backend.DTO.CommentDTO;
import com.ivanrogulj.Backend.DTO.PostDTO;
import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.Post;
import com.ivanrogulj.Backend.ExceptionHandler.DataNotFoundException;
import com.ivanrogulj.Backend.ExceptionHandler.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.ivanrogulj.Backend.Repositories.CommentRepository;
import com.ivanrogulj.Backend.Entities.Comment;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final DTOAssembler dtoAssembler;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService, DTOAssembler dtoAssembler) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
        this.dtoAssembler = dtoAssembler;
    }

    public CommentDTO createComment(CommentDTO commentDto, Long userId, Long postId) {
        if(!isAuthorizedToCreate(userId))
        {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
        UserDTO userDTO = userService.getUserById(userId);
        PostDTO postDTO = postService.getPostDtoById(postId);
        Post post = dtoAssembler.convertDtoToPost(postDTO);
        commentDto.setUser(userDTO);
        commentDto.setCreationDate(LocalDateTime.now());
        Comment comment = dtoAssembler.convertDtoToComment(commentDto);
        comment.setPost(post);
        commentRepository.save(comment);
        commentDto.setId(comment.getId());
        return commentDto;

    }

//    public Comment createComment(Comment comment, Long userId, Long postId) {
//        UserDTO userDTO = userService.getUserById(userId).orElse(null);
//        PostDTO postDTO = postService.getPostById(postId);
//        assert userDTO != null;
//        User user = entityToDtoMapper.convertDtoToUser(userDTO);
//        Post post = entityToDtoMapper.convertDtoToPost(postDTO);
//        if (user != null && post != null) {
//            comment.setUser(user);
//            comment.setPost(post);
//            comment.setCreationDate(LocalDateTime.now());
//            return commentRepository.save(comment);
//        }
//
//        return null;
//    }


    public Page<CommentDTO> getCommentsForPost(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId,pageable);
        return comments.map(dtoAssembler::convertToCommentDto);
    }

    public CommentDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException("Comment not found!"));
        return dtoAssembler.convertToCommentDto(comment);
    }

    public CommentDTO updateComment(Long commentId, CommentDTO updatedComment) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException("Comment not found!"));
        if(!comment.getContent().isEmpty())
        {
            comment.setContent(updatedComment.getContent());
        }
        CommentDTO commentDTO = dtoAssembler.convertToCommentDto(comment);
        commentRepository.save(comment);

        return commentDTO;
    }

    public void deleteComment(Long commentId) {
        if (!isAuthorizedToDelete(commentId)) {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException("Comment not found!"));
        commentRepository.deleteById(commentId);
    }


    public boolean isAuthorizedToDelete(Long commentId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return true;
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException ("Comment now found!"));

        String commentOwnerUsername = comment.getUser().getUsername();
        String postOwnerUsername = comment.getPost().getAuthor().getUsername();

        String currentUserUsername = userDetails.getUsername();

        return currentUserUsername.equals(commentOwnerUsername)
                || currentUserUsername.equals(postOwnerUsername);
    }

    private boolean isAuthorizedToCreate(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        return Objects.equals(userDetails.getUsername(), userService.getUserById(id).getUsername()) || isAdmin;
    }
}