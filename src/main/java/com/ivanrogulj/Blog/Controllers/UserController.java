package com.ivanrogulj.Blog.Controllers;
import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.DTO.UserLoginRequest;
import com.ivanrogulj.Blog.DTO.UserRegistrationRequest;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Services.EntityToDtoMapper;
import com.ivanrogulj.Blog.Services.UserService;
import com.ivanrogulj.Blog.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EntityToDtoMapper entityToDtoMapper;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, EntityToDtoMapper entityToDtoMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.entityToDtoMapper = entityToDtoMapper;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            UserDTO userDTO = entityToDtoMapper.convertUserToUserDTO(userOptional.get());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
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