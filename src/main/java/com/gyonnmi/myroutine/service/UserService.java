package com.gyonnmi.myroutine.service;

import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 메서드
    public void signup(String username, String password, String nickname) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("すでに使用されているユーザーIDです。");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);

        userRepository.save(user);
    }
}