package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

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
		List<Alarm> activeAlarms = alarmRepository.findByStatus(AlarmStatus.ACTIVE);

		for (Alarm alarm : activeAlarms) {
			if (productService.isPriceBelowDesired(alarm)) {
				String messageBody =
					alarm.getProduct().getName() + "의 가격이 " + alarm.getDesired_price() + "원 이하로 떨어졌습니다.";
				boolean notificationSent = sendNotification(alarm.getUser(), messageBody);
				if (notificationSent) {
					alarm.setStatus(AlarmStatus.INACTIVE);
					alarmRepository.save(alarm);
					log.info("알림이 성공적으로 전송되었습니다." + alarm.getUser().getAccount());
				}
			}
		}
	}

	private boolean sendNotification(User user, String messageBody) {

		List<FcmToken> fcmTokens = user.getFcmTokens();
		if (fcmTokens.isEmpty()) {
			return false;
		}
		boolean allNotificationsSent = true;
		for (FcmToken token : fcmTokens) {
			Message message = Message.builder()
				.setToken(token.getToken())
				.putData("title", "costFlower")
				.putData("body", messageBody)
				.build();
			try {
				firebaseMessaging.send(message);
				log.info("푸시 알림이 성공적으로 전송되었습니다");
			} catch (FirebaseMessagingException e) {
				log.error("푸시 알림 전송에 실패하였습니다", e);
				allNotificationsSent = false;
			}
		}
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