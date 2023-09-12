package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.DTO.UserDTO;
import com.ivanrogulj.Blog.Entities.Role;
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
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DTOAssembler dtoAssembler;
    @Autowired
    public UserService(UserRepository userRepository, DTOAssembler dtoAssembler) {
        this.userRepository = userRepository;
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
        if (user.getEmail() != null) {
            user.setEmail(userDto.getEmail());

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

    public Integer getUsersCount()
    {
        return userRepository.findAll().size();
    }

    public UserDTO searchUsersByUsername(String query) {
        User matchingUser = userRepository.findByUsernameContainingIgnoreCase(query);
        return dtoAssembler.convertUserToUserDTO(matchingUser);
    }

    public Page<UserDTO> searchUsersByFullName(String query, Pageable pageable) {
        Page<User> matchingUsers = userRepository.findByFullNameContainingIgnoreCase(query,pageable);
        return matchingUsers.map(dtoAssembler::convertUserToUserDTO);
    }

    public UserDTO getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        User user = userRepository.findByUsername(loggedInUsername).orElseThrow(() -> new DataNotFoundException("User not found!"));
        return dtoAssembler.convertUserToUserDTO(user);
    }

    public boolean isAdmin(UserDTO userDTO) {
        String roleNameToFind = "ROLE_ADMIN";
        Set<Role> roles = userDTO.getRoles();
        Role foundRole = roles.stream()
                .filter(role -> role.getName().equals(roleNameToFind))
                .findFirst().orElse(null);

        return foundRole != null;
    }

    private boolean isAuthorized(Long id) {
        UserDTO userDTO = getUserById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        return userDTO.getUsername().equals(authentication.getName()) || isAdmin;
    }

}
