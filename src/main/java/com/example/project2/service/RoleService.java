package com.example.project2.service;

import com.example.project2.model.Role;
import com.example.project2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role findOrCreate(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isPresent()) {
            return role.get();
        }
        Role newRole = new Role(roleName);
        return roleRepository.save(newRole);
    }
}

