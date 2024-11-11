package com.pricewagon.pricewagon.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable() // CSRF 보호 비활성화
			.authorizeHttpRequests()
			.requestMatchers("/", "/api/v1/**", "/swagger-ui/**", "/v3/api-docs/**")
			.permitAll() // 인증 없이 접근 가능한 경로 설정
			.requestMatchers("/admin/**")
			.hasRole("ADMIN") // ADMIN 역할 접근 제한
			.anyRequest()
			.authenticated() // 그 외 모든 요청에 대한 인증 요구
			.and()
			.httpBasic() // HTTP Basic Authentication 사용
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 비활성화, JWT와 함께 사용 가능

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
