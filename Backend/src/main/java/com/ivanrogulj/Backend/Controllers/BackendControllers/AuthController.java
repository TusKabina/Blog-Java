package com.ivanrogulj.Backend.Controllers.BackendControllers;

import com.ivanrogulj.Backend.DTO.UserDTO;
import com.ivanrogulj.Backend.DTO.UserLoginRequest;
import com.ivanrogulj.Backend.DTO.UserRegistrationRequest;
import com.ivanrogulj.Backend.Entities.Role;
import com.ivanrogulj.Backend.Entities.User;
import com.ivanrogulj.Backend.Repositories.RoleRepository;
import com.ivanrogulj.Backend.Repositories.UserRepository;
import com.ivanrogulj.Backend.Services.CustomUserDetailsService;
import com.ivanrogulj.Backend.Services.DTOAssembler;
import com.ivanrogulj.Backend.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
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

    private final DTOAssembler dtoAssembler;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, CustomUserDetailsService customUserDetailsService, UserService userService, PasswordEncoder passwordEncoder, DTOAssembler dtoAssembler) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.dtoAssembler = dtoAssembler;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username already exist!", HttpStatus.CONFLICT);
        }
        if(userRepository.existsByEmail(signUpDto.getEmail()))
        {
            return new ResponseEntity<>("User with that email already exist!", HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setFullName(signUpDto.getFullName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setEmail(signUpDto.getEmail());
        Role roles = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> authenticateUser(@RequestBody UserLoginRequest loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.getUserByUsername(loginDto.getUsername());
        UserDTO userDTO = dtoAssembler.convertUserToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.logout(request, response);
        } catch (IOException e) {
            System.out.println("Exception: "+e);
            return  ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}