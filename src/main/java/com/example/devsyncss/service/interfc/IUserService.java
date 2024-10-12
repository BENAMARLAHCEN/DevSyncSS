package com.example.devsyncss.service.interfc;

import com.example.devsyncss.entities.User;

import java.util.List;
import java.util.Map;

public interface IUserService {

    void addUser(User user, Long managerId);
    
    boolean registerUser(User user);

    List<User> getAllUsers();

    List<User> getAllManagers();

    List<User> getAllUsersByManagerId(Long managerId);

    User getUserByEmail(String email);

    User getUserById(Long managerId);

    boolean deleteUser(User user);

    boolean updateUser(User user);

    List<User> getUserByRole(String user);

    Long getLatestUserId();

}
