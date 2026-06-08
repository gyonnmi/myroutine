package com.gyonnmi.myroutine.service;

import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    // 필드
    private final UserRepository userRepository;

    // 생성자
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security가 로그인 시 자동으로 호출하는 메서드
     *
     * login.html에서 입력한 username으로
     * users 테이블의 회원을 조회하여 UserDetails 객체로 반환
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException { // username이 DB에 존재하지 않을 때 예외 발생

        // 사용자 이름으로 사용자 정보를 가져오는 메서드
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません。"));

        // User 엔티티의 username과 password를 사용하여 UserDetails 객체를 생성하여 반환
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}