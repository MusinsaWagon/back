package com.pricewagon.pricewagon.domain.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class JoinResultDTO {
		Long memberId;
		String account;
		LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class loginResultDTO {
		Long memberId;
		String account;
		String accessToken;
		LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class emailResultDTO {
		Integer validCode;

	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class emailCheckDTO {
		String email;
		boolean isDuplicated; // 중복 true, false : 중복 아님
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class validNumDTO {
		boolean isValid;
	}

}
