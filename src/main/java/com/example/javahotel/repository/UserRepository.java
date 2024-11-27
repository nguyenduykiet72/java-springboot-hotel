package com.example.javahotel.repository;

import com.example.javahotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    void delteByEmail(String email);

    Optional<User> findByEmail(String email);
}
