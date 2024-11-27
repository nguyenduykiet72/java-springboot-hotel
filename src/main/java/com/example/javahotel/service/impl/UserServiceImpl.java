package com.example.javahotel.service.impl;

import com.example.javahotel.entity.User;
import com.example.javahotel.repository.UserRepository;
import com.example.javahotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    

    @Override
    public User registerUser(User user) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String email) {

    }

    @Override
    public User getUser(String email) {
        return null;
    }
}
