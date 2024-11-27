package com.example.javahotel.service;

import com.example.javahotel.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);

}
