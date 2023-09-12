package com.ivanrogulj.Blog.Controllers;


import com.ivanrogulj.Blog.DTO.CategoryDTO;
import com.ivanrogulj.Blog.DTO.PostDTO;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Services.CategoryService;
import com.ivanrogulj.Blog.Services.PostService;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ExploreController {

    private final PostService postService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public ExploreController(PostService postService, UserService userService, CategoryService categoryService) {
        this.postService = postService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/explore")
    public String explorePage(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "category", required = false) Long categoryId,
                              @RequestParam(required = false) String sortBy, Model model) {

        Sort sort = null;
        if ("title".equals(sortBy)) {
            sort = Sort.by("title");
        } else if ("author".equals(sortBy)) {
            sort = Sort.by("author.fullName");
        } else if ("creationDate".equals(sortBy)) {
            sort = Sort.by("creationDate").descending();
        }

        Page<PostDTO> postsPage;
        UserDTO loggedInUser = userService.getLoggedInUser();
        List<CategoryDTO> categories = categoryService.getAllCategories();
        Pageable pageable = PageRequest.of(page, 4, sort != null ? sort : Sort.by("creationDate").descending());


        if (categoryId != null) {
            postsPage = postService.getPostsByCategory(categoryId, pageable);

        } else {
            postsPage = postService.getAllPosts(pageable);
        }

        boolean isAdmin = userService.isAdmin(loggedInUser);

        model.addAttribute("loggedInUser",loggedInUser);
        model.addAttribute("loggedIn", true);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("isAdmin",isAdmin);

        return "explore";
    }
}