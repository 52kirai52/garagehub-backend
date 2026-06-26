package com.garagehub.domain.user.service;

import com.garagehub.domain.user.dto.SignUpRequest;
import com.garagehub.domain.user.entity.User;
import com.garagehub.domain.user.repository.UserRepository;
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
            throw new RuntimeException("전화번호 인증이 필요합니다.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("이미 가입된 전화번호입니다.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .phoneVerified(true)
                .build();

        userRepository.save(user);
        redisTemplate.delete("sms:verified:" + request.getPhone());
    }

    public boolean checkUsername(String username) {
        return !userRepository.existsByUsername(username);
    }
}