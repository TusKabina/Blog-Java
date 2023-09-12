package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.DTO.CommentDTO;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Services.CommentService;
import com.ivanrogulj.Blog.Services.PostService;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        CommentDTO newComment = new CommentDTO();
        UserDTO loggedInUser = userService.getLoggedInUser();
        model.addAttribute("userId", loggedInUser.getId());
        model.addAttribute("comment", newComment);
        return "createComment";
    }

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
        Long authorOfPostId = post.getAuthor().getId();
        boolean isAdmin = userService.isAdmin(loggedInUser);

        System.out.println("AUTHOR OF POST"+ authorOfPostId + " loggedInUsser: " +loggedInUser);

        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("loggedIn", true);
        model.addAttribute("postId",postId);
        model.addAttribute("authorId",authorOfPostId);
        model.addAttribute("comments", commentDTO);

        return "comments";
    }

    @PostMapping("/delete/{postId}/{commentId}")
    public String deleteCommentOfPost(@PathVariable Long commentId, @PathVariable Long postId)
    {
        commentService.getCommentById(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/comment/view/" + postId;
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId)
    {
        commentService.getCommentById(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/home";
    }



    @GetMapping("/all-comments/{userId}")
    public String getAllComments(@PathVariable Long userId, Model model, @RequestParam(defaultValue = "0") int page) {

        Page<CommentDTO> commentsPage = commentService.getCommentsForUser(userId, PageRequest.of(page, 5));
        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);
        model.addAttribute("commentsPage", commentsPage);
        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("isAdmin", isAdmin);

        return "userComments";
    }

}