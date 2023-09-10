package com.ivanrogulj.Backend.Controllers;


import com.ivanrogulj.Backend.DTO.CategoryDTO;
import com.ivanrogulj.Backend.DTO.PostDTO;
import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.Role;
import com.ivanrogulj.Backend.Services.CategoryService;
import com.ivanrogulj.Backend.Services.PostService;
import com.ivanrogulj.Backend.Services.UserService;
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
import java.util.Set;

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

        int pageSize = 10;
        Sort.Direction direction = Sort.Direction.ASC;
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

        model.addAttribute("loggedInUser",loggedInUser);
        model.addAttribute("loggedIn", true);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sortBy", sortBy);

        String roleNameToFind = "ROLE_ADMIN";
        Set<Role> roles = loggedInUser.getRoles();
        Role foundRole = roles.stream()
                .filter(role -> role.getName().equals(roleNameToFind))
                .findFirst().orElse(null);

        boolean isAdmin = foundRole != null;

        model.addAttribute("isAdmin",isAdmin);

        return "explore"; // Return the name of your Explore HTML template
    }
}