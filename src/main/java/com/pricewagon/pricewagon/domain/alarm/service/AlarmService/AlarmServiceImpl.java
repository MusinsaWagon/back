package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.pricewagon.pricewagon.domain.alarm.dto.AlarmRequestDTO;
import com.pricewagon.pricewagon.domain.alarm.dto.AlarmResponseDTO;
import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.alarm.entity.AlarmStatus;
import com.pricewagon.pricewagon.domain.alarm.repository.AlarmRepository;
import com.pricewagon.pricewagon.domain.fcm.entity.FcmToken;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepository;
import com.pricewagon.pricewagon.domain.product.service.ProductService;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;
import com.pricewagon.pricewagon.global.error.exception.CustomException;
import com.pricewagon.pricewagon.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmServiceImpl implements AlarmService {

	private final ProductService productService;
	private final UserRepository userRepository;
	private final AlarmRepository alarmRepository;
	private final ProductRepository productRepository;
	private final FirebaseMessaging firebaseMessaging;

	@Override
	@Transactional
	@Scheduled(fixedRate = 120000)
	public void checkAllAlarmsAndNotify() {
		List<Alarm> activeAlarms = alarmRepository.findActiveAlarmsWithDetails(AlarmStatus.ACTIVE);

		for (Alarm alarm : activeAlarms) {
			try {
				processAlarm(alarm);
			} catch (Exception e) {
				log.error("알람 처리 중 오류 발생: alarmId={}, error={}", alarm.getId(), e.getMessage());
			}
		}
	}

	@Transactional
	protected void processAlarm(Alarm alarm) {
		Product product = alarm.getProduct();
		User user = alarm.getUser();

		if (productService.isPriceBelowDesired(alarm)) {
			String messageBody = String.format("%s의 가격이 %d원 이하로 떨어졌습니다.",
				product.getName(), product.getCurrentPrice());

			boolean notificationSuccess = sendNotificationToUser(user, messageBody);
			if (notificationSuccess) {
				alarm.setStatus(AlarmStatus.INACTIVE);
				alarmRepository.save(alarm);
				log.info("알림 전송 성공 후 알람 비활성화: user={}", user.getAccount());
			}
		}
	}

	@Transactional
	protected boolean sendNotificationToUser(User user, String messageBody) {
		List<FcmToken> tokens = new ArrayList<>(user.getFcmTokens());
		if (tokens.isEmpty()) {
			log.warn("푸시 알림 전송 실패: FCM 토큰이 없습니다. 사용자: {}", user.getAccount());
			return false;
		}

		List<FcmToken> tokensToRemove = new ArrayList<>(); // 별도의 리스트를 사용해서 삭제할 토큰 따로 저장
		boolean atLeastOneSuccess = false;

		for (FcmToken token : tokens) {
			try {
				Message message = Message.builder()
					.setToken(token.getToken())
					.putData("title", "costFlower")
					.putData("body", messageBody)
					.build();

				firebaseMessaging.send(message);
				atLeastOneSuccess = true;
				log.info("푸시 알림 전송 성공: user={}, token={}", user.getAccount(), token.getToken());
			} catch (FirebaseMessagingException e) {
				log.error("푸시 알림 전송 실패: user={}, token={}, error={}",
					user.getAccount(), token.getToken(), e.getMessage());

				if ("UNREGISTERED".equals(e.getMessagingErrorCode().name())) {
					tokensToRemove.add(token);
					log.warn("유효하지 않은 FCM 토큰 발견: user={}, token={}",
						user.getAccount(), token.getToken());
				}
			}
		}

		if (!tokensToRemove.isEmpty()) {
			removeInvalidTokens(user, tokensToRemove);
		}

		return atLeastOneSuccess;
	}

	@Transactional
	protected void removeInvalidTokens(User user, List<FcmToken> tokensToRemove) {
		tokensToRemove.forEach(token -> {
			user.getFcmTokens().remove(token);

		});
		userRepository.save(user);
		log.info("유효하지 않은 FCM 토큰들 제거 완료: user={}, removedCount={}",
			user.getAccount(), tokensToRemove.size());
	}

	@Override
	@Transactional
	public AlarmResponseDTO.registerAlarmDTO registerAlarm(AlarmRequestDTO.registerAlarm request, String username) {
		Product product = productRepository.findByProductNumber(request.getProductNumber())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		User user = userRepository.findByAccount(username)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		if (request.getFcmToken() != null && !request.getFcmToken().isEmpty()) {
			registerFcmToken(user, request.getFcmToken());
		}

		Alarm alarm = Alarm.builder()
			.user(user)
			.product(product)
			.desired_price(request.getPrice())
			.status(AlarmStatus.ACTIVE)
			.build();

		alarmRepository.save(alarm);
		product.updateAlarmCount(product.getAlarmCount() + 1);

		return AlarmResponseDTO.registerAlarmDTO.builder()
			.price(request.getPrice())
			.account(username)
			.productNumber(request.getProductNumber())
			.build();
	}

	@Transactional
	protected void registerFcmToken(User user, String tokenString) {
		boolean tokenExists = user.getFcmTokens().stream()
			.anyMatch(token -> token.getToken().equals(tokenString));

		if (!tokenExists) {
			FcmToken fcmToken = FcmToken.builder()
				.token(tokenString)
				.user(user)
				.build();
			user.addFcmToken(fcmToken);
			userRepository.save(user);
			log.info("새로운 FCM 토큰 등록: user={}, token={}", user.getAccount(), tokenString);
		}
	}
}