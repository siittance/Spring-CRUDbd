package com.example.project2.config;

import com.example.project2.model.Role;
import com.example.project2.model.User;
import com.example.project2.repository.RoleRepository;
import com.example.project2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Создаем роли, если их нет
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(new Role("ADMIN"));
        }
        if (roleRepository.findByName("MANAGER").isEmpty()) {
            roleRepository.save(new Role("MANAGER"));
        }
        if (roleRepository.findByName("USER").isEmpty()) {
            roleRepository.save(new Role("USER"));
        }

        // Создаем администратора по умолчанию, если его нет
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEnabled(true);
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            admin.setRoles(Arrays.asList(adminRole));
            userRepository.save(admin);
        }
    }
}

