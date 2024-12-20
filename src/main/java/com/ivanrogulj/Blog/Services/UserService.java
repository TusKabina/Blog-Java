package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.ExceptionHandler.DataNotFoundException;
import com.ivanrogulj.Blog.ExceptionHandler.ForbiddenException;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DTOAssembler dtoAssembler;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DTOAssembler dtoAssembler) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dtoAssembler = dtoAssembler;
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(dtoAssembler::convertUserToUserDTO);
    }

    public UserDTO getUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found!"));
        return dtoAssembler.convertUserToUserDTO(user);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("User not found!"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


    public UserDTO updateUser(Long id, UserDTO userDto) {
        if(!isAuthorized(id))
        {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found!"));
        if (userDto.getFullName() != null) {
            user.setFullName(userDto.getFullName());

        }
        if (user.getFullName() != null) {
            user.setUsername(userDto.getUsername());

        }
        userRepository.save(user);
        return dtoAssembler.convertUserToUserDTO(user);
    }

    public void deleteUser(Long id) {
        if(!isAuthorized(id))
        {
            throw new ForbiddenException("You are not authorized for this operation!");
        }
        userRepository.deleteById(id);
    }


    public UserDTO searchUsersByUsername(String query) {
        User matchingUser = userRepository.findByUsernameContainingIgnoreCase(query);
        return dtoAssembler.convertUserToUserDTO(matchingUser);
    }

    public Page<UserDTO> searchUsersByFullName(String query, Pageable pageable) {
        Page<User> matchingUsers = userRepository.findByFullNameContainingIgnoreCase(query,pageable);
        return matchingUsers.map(dtoAssembler::convertUserToUserDTO);
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
                if (cookie.getName().equals("JSESSIONID")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // Expire the cookie
                    response.addCookie(cookie);
                }
            }
        }
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            throw new IOException("Exception while trying to redirect user to login!");
        }
    }

    private boolean isAuthorized(Long id) {
        UserDTO userDTO = getUserById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        return userDTO.getUsername().equals(authentication.getName()) || isAdmin;
    }
}
