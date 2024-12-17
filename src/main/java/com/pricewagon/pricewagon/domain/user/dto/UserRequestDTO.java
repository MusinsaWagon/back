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
		String account;

		@NotBlank
		String password;

	}

	@Getter
	public static class emailRequestDTO {
		@NotBlank
		@Email
		String account;
	}

	@Getter
	public static class checkEmailDTO {
		@NotBlank
		@Email
		String email;
	}

	@Getter
	public static class validNumDTO {
		@Email
		@NotBlank
		String email;

		@NotBlank
		String validCode;
	}
}
