package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User getUserByEmail(String email);
    void addUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void removeUserById(Long id);
    List<User> listUsers();
}
