package com.pricewagon.pricewagon.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
	private final CustomUserDetailService customUserDetailService;
	private final JwtUtil jwtUtil;
	// private final AuthenticationEntryPoint authenticationEntryPoint;
	// private final AccessDeniedHandler accessDeniedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 및 CORS 설정
		http.csrf(csrf -> csrf.disable());
		http.cors(Customizer.withDefaults());

		// 세션 관리 - STATELESS
		http.sessionManagement(
			sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// FormLogin, BasicHttp 비활성화
		http.formLogin(form -> form.disable());
		http.httpBasic(AbstractHttpConfigurer::disable);

		// JwtAuthFilter 추가
		http.addFilterBefore(new JwtAuthFilter(customUserDetailService, jwtUtil),
			UsernamePasswordAuthenticationFilter.class);

		// // 예외 처리
		// http.exceptionHandling(exceptionHandling -> exceptionHandling
		// 	.authenticationEntryPoint(authenticationEntryPoint)
		// 	.accessDeniedHandler(accessDeniedHandler)
		// );

		// 권한 규칙 설정
		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/", "/api/v1/**", "/swagger-ui/**", "/v3/api-docs/**", "/auth/login/**")
			.permitAll() // 화이트리스트에 등록된 경로는 인증 없이 접근 가능
			.requestMatchers("/admin/**")
			.hasRole("ADMIN") // 관리자 권한 제한
			.anyRequest()
			.permitAll() // 인증 처리 Pass
		);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
}
