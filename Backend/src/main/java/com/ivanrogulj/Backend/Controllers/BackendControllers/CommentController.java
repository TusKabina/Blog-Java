package com.ivanrogulj.Backend.Controllers.BackendControllers;

import com.ivanrogulj.Backend.DTO.CommentDTO;
import com.ivanrogulj.Backend.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long postId, @RequestParam Long userId, @RequestBody CommentDTO commentDTO) {

        CommentDTO createdComment = commentService.createComment(commentDTO, userId, postId);
        if (createdComment != null) {
            return ResponseEntity.ok(createdComment);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PostMapping("/posts/{postId}/comments")
//    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @RequestParam Long userId, @RequestBody Comment comment) {
//
//        Comment createdComment = commentService.createComment(comment, userId, postId);
//        if (createdComment != null) {
//            return ResponseEntity.ok(createdComment);
//        } else {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<Page<CommentDTO>> getCommentsForPost(@PathVariable Long postId, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<CommentDTO> comments = commentService.getCommentsForPost(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @GetMapping("comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
       CommentDTO comment = commentService.getCommentById(commentId);
        return  ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @PutMapping("comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO updatedComment) {
        CommentDTO comment = commentService.updateComment(commentId, updatedComment);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}