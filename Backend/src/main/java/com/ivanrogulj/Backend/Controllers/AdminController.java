package com.ivanrogulj.Backend.Controllers;

import com.ivanrogulj.Backend.DTO.CategoryDTO;
import com.ivanrogulj.Backend.DTO.PostDTO;
import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.Entities.Category;
import com.ivanrogulj.Backend.Services.CategoryService;
import com.ivanrogulj.Backend.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;


    private final CategoryService categoryService;

    public AdminController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping
    public String adminDashboard(Model model) {
        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);

        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("userCount", userService.getUsersCount());
        return "admin";
    }
    @GetMapping("/view-users")
    public String viewUsers(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

        int size = 10;
        Page<UserDTO> usersPage = userService.getAllUsers(PageRequest.of(page, size));
        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);

        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("users", usersPage);

        return "viewUsers";
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/view-users";
    }

    @GetMapping("/create-category")
    public String getCreateCategoryForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        // You can add any attributes you want to pass to the view
        return "createCategory";
    }

    @PostMapping("/create-category")
    public String createCategory( @ModelAttribute CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        return "redirect:/admin/view-categories";
    }

    @GetMapping("/update-category/{id}")
    public String getUpdateCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        model.addAttribute("category", categoryDTO);
        return "updateCategory";
    }

    @PostMapping("/update-category/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return "redirect:/admin/view-categories";
    }

    @GetMapping("/view-categories")
    public String viewAllCategories(Model model) {
        UserDTO loggedInUserDetails = userService.getLoggedInUser();
        List<CategoryDTO> categories = categoryService.getAllCategories();
        boolean isAdmin = userService.isAdmin(loggedInUserDetails);

        model.addAttribute("loggedInUser", loggedInUserDetails);
        model.addAttribute("isAdmin",isAdmin);
        model.addAttribute("categories",categories);
        return "viewAllCategories";
    }

}