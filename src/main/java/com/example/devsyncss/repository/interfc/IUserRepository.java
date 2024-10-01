package com.example.devsyncss.repository.interfc;

import com.example.devsyncss.entities.User;

import java.util.List;

public interface IUserRepository {
    void save(User user);

    User findById(Long id);

    List<User> findAll();

    void update(User user);

    void delete(Long id);

    User findByEmail(String email);
}
