package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Optional;

public interface UserService {
     Iterable<User> findAll();
     User findUserById(Long id);
     User save(User user);
     User update(Long id, User newUser);
     void remove(Long id);
}
