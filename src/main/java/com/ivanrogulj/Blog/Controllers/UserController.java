package com.ivanrogulj.Blog.Controllers;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.Like;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Services.EntityToDtoMapper;
import com.ivanrogulj.Blog.Services.LikeService;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final LikeService likeService;
    private final EntityToDtoMapper entityToDtoMapper;


    @Autowired
    public UserController(UserService userService, LikeService likeService, EntityToDtoMapper entityToDtoMapper) {
        this.userService = userService;
        this.likeService = likeService;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    @GetMapping("")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        Optional<UserDTO> userOptional = userService.getUserById(id);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        UserDTO updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}