package com.example.project2.service;

import com.example.project2.model.Role;
import com.example.project2.model.User;
import com.example.project2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createUserWithRole(String username, String password, String roleName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        Role role = roleService.findOrCreate(roleName);
        user.setRoles(Arrays.asList(role));
        return save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}

