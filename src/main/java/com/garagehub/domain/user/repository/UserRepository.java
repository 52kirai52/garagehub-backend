package com.garagehub.domain.user.repository;

import com.garagehub.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}