package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityToDtoMapper entityToDtoMapper;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EntityToDtoMapper entityToDtoMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(entityToDtoMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
      Optional<User> user = userRepository.findById(id);
        Optional<UserDTO> userDTO = user.map(entityToDtoMapper::convertUserToUserDTO);
        return userDTO;
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }


    public UserDTO updateUser(Long id, UserDTO userDto) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(userDto.getId()).get();
            if(userDto.getFullName() != null)
            {
                user.setFullName(userDto.getFullName());

            }
            if (user.getFullName() != null)
            {
                user.setUsername(userDto.getUsername());

            }
            userRepository.save(user);
            return entityToDtoMapper.convertUserToUserDTO(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        return userRepository.findByUsername(loggedInUsername);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Assuming you have a specific cookie name for authentication
                if (cookie.getName().equals("authCookie")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // Expire the cookie
                    response.addCookie(cookie);
                }
            }
        }
        try {
            response.sendRedirect("/auth/login");
        } catch (IOException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }
    }

}
