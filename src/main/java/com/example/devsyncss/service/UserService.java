package com.example.devsyncss.service;

import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.Role;
import com.example.devsyncss.repository.UserRepository;
import com.example.devsyncss.repository.interfc.IUserRepository;
import com.example.devsyncss.service.interfc.IUserService;

import java.util.List;

public class UserService implements IUserService {


    private final IUserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public void addUser(User user, Long managerId) {
        if (managerId != null) {
            User manager = userRepository.findById(managerId);
            if (manager != null && manager.getRole() == Role.MANAGER) {
                user.setManager(manager);
            } else {
                throw new IllegalArgumentException("Invalid manager ID or manager not found");
            }
        }
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email){
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return userRepository.findById(id);
    }
}
