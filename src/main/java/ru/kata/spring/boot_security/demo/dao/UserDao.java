package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUserName(String name);
    void saveUser(User user);
    User update(User user);
    void delete(Long id);
}
