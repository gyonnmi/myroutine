package com.gyonnmi.myroutine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // CSRF 보안 기능 끄기
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/signup",
                                "/login")
                        .permitAll() // 위 경로는 인증 없이 접근 허용
                        .anyRequest().authenticated()) // 그 외 모든 요청은 인증 필요
                .formLogin(form -> form // 폼 기반 로그인 설정
                        .loginPage("/login") // 로그인 페이지 URL
                        .loginProcessingUrl("/login") // 로그인 버튼 클릭시 Spring Security가 처리할 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 이동할 URL, true는 항상 이 URL로 이동하도록 설정
                        .failureUrl("/login?error") // 로그인 실패 시 이동할 URL
                        .permitAll()) 
                .logout(logout -> logout // 로그아웃 설정
                        .logoutUrl("/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/login?logout") // 로그아웃 성공 시 이동할 URL
                        .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // 비밀번호 암호화 설정
        return new BCryptPasswordEncoder();
    }
}