package com.example.devsyncss.repository.interfc;

import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.Role;

import java.util.List;
import java.util.Map;

public interface IUserRepository {
    void save(User user);

    User findById(Long id);

    List<User> findAll();

    void update(User user);

    void delete(Long id);

    User findByEmail(String email);

    List<User> findAllUsersByRole(Role role);

    List<User> findAllUsersByManagerId(Long managerId);

    Long findLatestUserId();
}
