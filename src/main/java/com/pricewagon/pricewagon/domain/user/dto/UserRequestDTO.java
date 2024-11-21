package com.pricewagon.pricewagon.domain.user.dto;

import com.pricewagon.pricewagon.domain.user.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

	@Getter
	@Setter
	public static class JoinDto {
		@NotBlank
		@Email
		String account;

		@NotBlank
		String password;

		@NotNull
		UserRole role;

	}

	@Getter
	@Setter
	public static class loginDto {
		@NotBlank
		@Email
		String account;

		@NotBlank
		String password;

	}
}
