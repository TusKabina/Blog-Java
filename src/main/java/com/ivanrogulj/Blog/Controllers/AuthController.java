package com.ivanrogulj.Blog.Controllers;

import com.ivanrogulj.Blog.DTO.UserLoginRequest;
import com.ivanrogulj.Blog.DTO.UserRegistrationRequest;
import com.ivanrogulj.Blog.Entities.Role;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Repositories.RoleRepository;
import com.ivanrogulj.Blog.Repositories.UserRepository;
import com.ivanrogulj.Blog.Services.CustomUserDetailsService;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, CustomUserDetailsService customUserDetailsService, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userService.createUser(user);
//        return ResponseEntity.ok("User registered successfully");
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username already exist!", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setFullName(signUpDto.getFullName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody UserLoginRequest loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
    }
}