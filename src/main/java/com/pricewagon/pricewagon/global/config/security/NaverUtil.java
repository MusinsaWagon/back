package com.pricewagon.pricewagon.global.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricewagon.pricewagon.domain.user.dto.NaverDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NaverUtil {
	@Value("${naver.client_id}")
	private String naverClientId;
	@Value("${naver.client_secret}")
	private String naverClientSecret;
	@Value("${naver.redirect_uri}")
	private String naverRedirectUri;

	public NaverDTO.OAuthToken requestToken(String accessCode) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", naverClientId);
		params.add("client_secret", naverClientSecret);
		params.add("code", accessCode);
		params.add("state", "random_state_value");

		HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			"https://nid.naver.com/oauth2.0/token",
			HttpMethod.POST,
			naverTokenRequest,
			String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		NaverDTO.OAuthToken oAuthToken = null;

		try {
			oAuthToken = objectMapper.readValue(response.getBody(), NaverDTO.OAuthToken.class);
			log.info("oAuthToken : " + oAuthToken.getAccess_token());
		} catch (JsonProcessingException e) {
			throw new RuntimeException("네이버 토큰 정보 파싱 실패");
		}
		return oAuthToken;
	}

	public NaverDTO.UserInfo requestUserInfo(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);

		HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
			"https://openapi.naver.com/v1/nid/me",
			HttpMethod.POST,
			naverUserInfoRequest,
			String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		NaverDTO.UserInfo userInfo = null;

		try {
			userInfo = objectMapper.readValue(response.getBody(), NaverDTO.UserInfo.class);
			log.info("userInfo : " + userInfo.getResponse().getEmail());
		} catch (JsonProcessingException e) {
			throw new RuntimeException("네이버 유저 정보 파싱 실패");
		}
		return userInfo;
	}
}
