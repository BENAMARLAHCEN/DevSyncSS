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

    public boolean registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }
        userRepository.save(user);
        return true;
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

    public boolean deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        try {
            userRepository.delete(user.getId());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        try {
            userRepository.update(user);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
