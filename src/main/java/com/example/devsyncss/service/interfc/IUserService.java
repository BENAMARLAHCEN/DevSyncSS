package com.example.devsyncss.service.interfc;

import com.example.devsyncss.entities.User;

import java.util.List;

public interface IUserService {

    void addUser(User user, Long managerId);
    
    boolean registerUser(User user);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getUserById(Long managerId);
}
