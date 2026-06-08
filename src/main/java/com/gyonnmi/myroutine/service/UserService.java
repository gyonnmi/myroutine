package com.gyonnmi.myroutine.service;

import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.repository.RoutineLogRepository;
import com.gyonnmi.myroutine.repository.RoutineRepository;
import com.gyonnmi.myroutine.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    // 필드
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;

    // 생성자
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoutineRepository routineRepository,
            RoutineLogRepository routineLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.routineRepository = routineRepository;
        this.routineLogRepository = routineLogRepository;
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

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));

        user.setNickname(nickname);
    }

    @Transactional
    public void updatePassword(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));

        user.setPassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void deleteAccount(Long userId) {
        // 1. 회원의 루틴 완료 이력 삭제
        routineLogRepository.deleteByRoutine_User_Id(userId);

        // 2. 회원의 루틴 삭제
        routineRepository.deleteByUser_Id(userId);

        // 3. 회원 삭제
        userRepository.deleteById(userId);
    }
}