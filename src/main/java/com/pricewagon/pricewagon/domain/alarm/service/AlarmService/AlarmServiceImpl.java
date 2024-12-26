package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import java.util.Iterator;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
	@Async
	@Scheduled(fixedRate = 60000) // Every 1 minute
	public void checkAllAlarmsAndNotify() {
		List<Alarm> activeAlarms = alarmRepository.findActiveAlarmsWithDetails(AlarmStatus.ACTIVE);

		for (Alarm alarm : activeAlarms) {
			Product product = alarm.getProduct();
			User user = alarm.getUser();

			if (productService.isPriceBelowDesired(alarm)) {
				String messageBody =
					product.getName() + "의 가격이 " + product.getCurrentPrice() + "원 이하로 떨어졌습니다.";
				boolean notificationSent = sendNotification(user, messageBody);
				if (notificationSent) {
					alarm.setStatus(AlarmStatus.INACTIVE);
					alarmRepository.save(alarm);
					log.info("알림이 성공적으로 전송되었습니다." + user.getAccount());
				}
			}
		}
	}

	private boolean sendNotification(User user, String messageBody) {

		List<FcmToken> fcmTokens = user.getFcmTokens();
		if (fcmTokens == null || fcmTokens.isEmpty()) {
			log.warn("푸시 알림 전송 실패: FCM 토큰이 없습니다. 사용자: {}", user.getAccount());
			return false;
		}

		boolean allNotificationsSent = true;

		log.debug("푸시 알림 전송 시작: 사용자: {}", user.getAccount());

		Iterator<FcmToken> iterator = fcmTokens.iterator();
		while (iterator.hasNext()) {
			FcmToken token = iterator.next();
			log.debug("푸시 알림 전송 중: 사용자: {}, FCM 토큰: {}", user.getAccount(), token.getToken());

			Message message = Message.builder()
				.setToken(token.getToken())
				.putData("title", "costFlower")
				.putData("body", messageBody)
				.build();

			try {
				firebaseMessaging.send(message);
				log.info("푸시 알림이 성공적으로 전송되었습니다. 사용자: {}, FCM 토큰: {}", user.getAccount(), token.getToken());
			} catch (FirebaseMessagingException e) {
				log.error("푸시 알림 전송 실패: 사용자: {}, FCM 토큰: {}, 이유: {}", user.getAccount(), token.getToken(),
					e.getMessage());

				// UNREGISTERED 에러 처리: 무효화된 토큰 제거
				if ("UNREGISTERED".equals(e.getMessagingErrorCode().name())) {
					log.warn("유효하지 않은 FCM 토큰을 제거합니다. 사용자: {}, FCM 토큰: {}", user.getAccount(), token.getToken());
					iterator.remove();
					user.removeFcmToken(token);
				}

				allNotificationsSent = false;
			}
		}

		if (!allNotificationsSent) {
			userRepository.save(user);
			log.info("FCM 토큰 변경사항이 사용자 데이터베이스에 저장되었습니다. 사용자: {}", user.getAccount());
		}

		log.debug("푸시 알림 전송 완료: 사용자: {}, 결과: {}", user.getAccount(), allNotificationsSent);
		return allNotificationsSent;
	}

	@Override
	public AlarmResponseDTO.registerAlarmDTO registerAlarm(AlarmRequestDTO.registerAlarm request, String username) {
		Product product = productRepository.findById(request.getProductId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		User user = userRepository.findByAccount(username)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		if (request.getFcmToken() != null && !request.getFcmToken().isEmpty()) {
			if (user.getFcmTokens().stream().noneMatch(token -> token.getToken().equals(request.getFcmToken()))) {
				FcmToken fcmToken = FcmToken.builder()
					.token(request.getFcmToken())
					.user(user)
					.build();
				user.addFcmToken(fcmToken);
			}
		}

		Alarm alarm = Alarm.builder()
			.user(user)
			.product(product)
			.desired_price(request.getPrice())
			.status(AlarmStatus.ACTIVE)
			.build();
		alarmRepository.save(alarm);

		return AlarmResponseDTO.registerAlarmDTO.builder()
			.price(request.getPrice())
			.account(username)
			.productId(request.getProductId())
			.build();
	}

}