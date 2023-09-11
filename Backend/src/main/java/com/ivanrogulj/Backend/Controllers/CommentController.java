package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.CommentDTO;
import com.ivanrogulj.Backend.DTO.PostDTO;
import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.Role;
import com.ivanrogulj.Backend.Services.CommentService;
import com.ivanrogulj.Backend.Services.PostService;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/create/{postId}")
    public String getCreateCommentForm(@PathVariable Long postId, Model model) {
        // Create a new CommentDTO for the form
        CommentDTO newComment = new CommentDTO();
        UserDTO loggedInUser = userService.getLoggedInUser();

        // Pass the user ID and post ID to the Thymeleaf template
        model.addAttribute("userId", loggedInUser.getId());
        model.addAttribute("comment", newComment);
        return "createComment";
    }

    // Endpoint to handle the submission of a new comment
    @PostMapping("/create/{postId}")
    public String createComment(@ModelAttribute CommentDTO comment, @RequestParam Long userId, @PathVariable Long postId) {
        commentService.createComment(comment, userId, postId);

        return "redirect:/post/{postId}";
    }

    @GetMapping("/view/{postId}")
    public String viewCommentsOfPost(@PathVariable Long postId, @RequestParam(name = "page", defaultValue = "0") int page, Model model)
    {
        UserDTO loggedInUser = userService.getLoggedInUser();
        Page<CommentDTO> commentDTO = commentService.getCommentsForPost(postId, PageRequest.of(page, 4));
        PostDTO post = postService.getPostById(postId);
        Long authorId = post.getAuthor().getId();
        boolean isAdmin = userService.isAdmin(loggedInUser);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("loggedIn", true);
        model.addAttribute("postId",postId);
        model.addAttribute("authorId",authorId);
        model.addAttribute("comments", commentDTO);

        return "comments";
    }

    @PostMapping("/delete/{postId}/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @PathVariable Long postId)
    {
        commentService.getCommentById(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/comment/view/" + postId;
    }

}