package com.pricewagon.pricewagon.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity // Spring Security 설정 활성화
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((requests)-> requests //http요청에 대한 접근 제어 설정
                        .requestMatchers("/","/join").permitAll()// URL 패턴에 대한 접근 권한 설정, permitAll은 인증 없이 접근 가능한 경로 지정
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 역할을 가진 사용자만 접근 가능하도록 제한
                        .anyRequest().authenticated() // 그 외 모든 요청에 대한 인증 요구
                )
                .formLogin((form)-> form.loginPage("/login") //커스텀 로그인 페이지를 /login 경로로 지정
                        .defaultSuccessUrl("/home", true) // 성공하면 /home으로 리다이렉트
                        .permitAll() // 모든 사용자가 접근 가능하도록 설정
                )
                .logout((logout)-> logout
                        .logoutUrl("/logout") // 로그아웃 처리
                        .logoutSuccessUrl("/login?logout") // 로그아웃 성공하면 해당 주소로 리다이렉트
                        .permitAll()
                );
        return http.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){ // 비밀번호를 암호화 하여 저장하기 위함
        return new BCryptPasswordEncoder();
    }
}
