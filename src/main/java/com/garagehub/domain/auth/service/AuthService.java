package com.garagehub.domain.auth.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.garagehub.domain.user.repository.UserRepository;
import com.garagehub.global.exception.CustomException;
import com.garagehub.global.exception.ErrorCode;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;

@Service
public class AuthService {

    private static final Duration COOLDOWN = Duration.ofSeconds(30);
    private static final Duration LOCK_TTL = Duration.ofSeconds(5);
    private static final Duration CODE_TTL  = Duration.ofMinutes(5);
    private static final Duration VERIFY_TTL = Duration.ofMinutes(10);

    private final SmsService smsService;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(SmsService smsService, StringRedisTemplate redisTemplate, UserRepository userRepository) {
        this.smsService = smsService;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    public void sendVerificationCode(String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE);
        }

        String lockKey = "sms:lock:" + phone;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", LOCK_TTL);
        if (!Boolean.TRUE.equals(locked)) {
            throw new CustomException(ErrorCode.SMS_LOCK);
        }

        try {
            String cooldownKey = "sms:cooldown:" + phone;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))) {
                Long ttl = redisTemplate.getExpire(cooldownKey);
                throw new CustomException(ErrorCode.SMS_COOLDOWN, Map.of("remainingSeconds", ttl));
            }

            String codeKey = "sms:code:" + phone;
            redisTemplate.delete(codeKey);

            String code = generateCode();
            smsService.sendSms(phone, "[GarageHub] 인증번호: " + code);

            redisTemplate.opsForValue().set(codeKey, code, CODE_TTL);
            redisTemplate.opsForValue().set(cooldownKey, "1", COOLDOWN);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SMS_SEND_FAILED);
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    public boolean verifyCode(String phone, String code) {
        String stored = redisTemplate.opsForValue().get("sms:code:" + phone);
        if (stored == null || !stored.equals(code)) {
            return false;
        }
        redisTemplate.delete("sms:code:" + phone);
        redisTemplate.opsForValue().set(
            "sms:verified:" + phone, "true", VERIFY_TTL
        );
        return true;
    }

    private String generateCode() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }
}