package com.example.javahotel.controller;

import com.example.javahotel.entity.User;
import com.example.javahotel.exception.UserAlreadyExistsException;
import com.example.javahotel.security.jwt.JwtUtils;
import com.example.javahotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registered Successfully");
        } catch (UserAlreadyExistsException uae) {
            return  ResponseEntity.badRequest().body(uae.getMessage());
        }
    }
}
