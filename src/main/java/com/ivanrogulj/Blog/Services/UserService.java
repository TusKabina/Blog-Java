package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.ExceptionHandler.BadRequestException;
import com.ivanrogulj.Blog.ExceptionHandler.DataNotFoundException;
import com.ivanrogulj.Blog.Repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
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

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(entityToDtoMapper::convertUserToUserDTO);
    }

    public UserDTO getUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found!"));
        return entityToDtoMapper.convertUserToUserDTO(user);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("User not found!"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


    public UserDTO updateUser(Long id, UserDTO userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found!"));
        if (userDto.getFullName() != null) {
            user.setFullName(userDto.getFullName());

        }
        if (user.getFullName() != null) {
            user.setUsername(userDto.getUsername());

        }
        userRepository.save(user);
        return entityToDtoMapper.convertUserToUserDTO(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        return userRepository.findByUsername(loggedInUsername).orElseThrow(() -> new DataNotFoundException("User not found!"));
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            throw new IOException("Exception while trying to redirect user to login!");
        }
    }

}
