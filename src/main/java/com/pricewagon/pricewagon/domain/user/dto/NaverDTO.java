package com.pricewagon.pricewagon.domain.user.dto;

import lombok.Getter;

public class NaverDTO {
	@Getter
	public static class OAuthToken {
		String access_token;
		String token_type;
		String refresh_token;
		Long expires_in;
		String error;
		String error_description;

	}

	@Getter
	public static class UserInfo {
		String resultcode;
		String message;
		Response response;

		@Getter
		public static class Response {
			String id;
			String nickname;
			String name;
			String email;
			String gender;
			String age;
			String birthday;
			String profile_image;
			String birthyear;

		}
	}
}
