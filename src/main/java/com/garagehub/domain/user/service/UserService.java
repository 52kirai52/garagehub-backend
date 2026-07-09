package com.garagehub.domain.user.service;

import com.garagehub.domain.user.dto.SignUpRequest;
import com.garagehub.domain.user.entity.User;
import com.garagehub.domain.user.repository.UserRepository;
import com.garagehub.global.exception.CustomException;
import com.garagehub.global.exception.ErrorCode;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       StringRedisTemplate redisTemplate,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signUp(SignUpRequest request) {
        String verified = redisTemplate.opsForValue().get("sms:verified:" + request.getPhone());
        if (!"true".equals(verified)) {
            throw new CustomException(ErrorCode.PHONE_NOT_VERIFIED);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .phoneVerified(true)
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .email(request.getEmail())
                .build();

        userRepository.save(user);
        redisTemplate.delete("sms:verified:" + request.getPhone());
    }

    public boolean checkUsername(String username) {
        return !userRepository.existsByUsername(username);
    }
}