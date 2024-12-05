package com.pricewagon.pricewagon.domain.user.dto;

import lombok.Getter;

public class KakaoDTO {
	@Getter
	public static class OAuthToken {
		String access_token;
		String token_type;
		String refresh_token;
		Long expires_in;
		String scope;
		Long refresh_token_expires_in;

	}

	@Getter
	public static class KakaoProfile {
		Long id;
		String connected_at;
		Properties properties;
		KakaoAccount kakao_account;

		@Getter
		public static class Properties {
			String nickname;
			String profile_image;
			String thumbnail_image;
		}

		@Getter
		public static class KakaoAccount {
			Boolean profile_nickname_needs_agreement;
			Boolean profile_image_needs_agreement;
			Profile profile;
			Boolean has_email;
			Boolean email_needs_agreement;
			Boolean is_email_valid;
			Boolean is_email_verified;
			String email;

			@Getter
			public static class Profile {
				String nickname;
				String thumbnail_image_url;
				String profile_image_url;
				Boolean is_default_image;
				Boolean is_default_nickname;
			}
		}
	}
}
