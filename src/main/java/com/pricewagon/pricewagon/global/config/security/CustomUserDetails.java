package com.pricewagon.pricewagon.global.config.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pricewagon.pricewagon.domain.user.dto.CustomUserInfoDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private final CustomUserInfoDto user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of("ROLE_" + user.getRole())
			.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return user.getPassword(); // 인코딩된 비밀번호 반환
	}

	@Override
	public String getUsername() {
		return user.getEmail(); // 계정 이름이나 이메일 반환
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

