package com.pricewagon.pricewagon.global.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FCMConfig {

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {
		// 경로 수정: "resources/" 생략
		ClassPathResource resource = new ClassPathResource(
			"firebase/fcm.json");
		InputStream refreshToken = resource.getInputStream();

		FirebaseApp firebaseApp = null;
		List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

		// FirebaseApp 존재 여부 확인
		if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
			for (FirebaseApp app : firebaseAppList) {
				if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
					firebaseApp = app;
					log.info("파이어베이스 초기화: {}", app.getName());
				}
			}
		}

		// FirebaseApp 초기화
		if (firebaseApp == null) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(refreshToken))
				.build();
			firebaseApp = FirebaseApp.initializeApp(options);
			log.info("파이어베이스 초기화: {}", firebaseApp.getName());
		}

		FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
		log.info("파이어베이스 성공적으로 생성완료");

		return firebaseMessaging;
	}
}