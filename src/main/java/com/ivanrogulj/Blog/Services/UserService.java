package com.ivanrogulj.Blog.Services;

import com.ivanrogulj.Blog.Entities.User;
import com.ivanrogulj.Blog.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }



//    public User registerUser(UserRegistrationRequest request) {
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setFullName(request.getFullName());
//
//        return userRepository.save(user);
//    }
//
//    public User loginUser(String username, String password) {
//        User user = userRepository.findByUsername(username);
//
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return user;
//        }
//
//        return null;
//    }
}
