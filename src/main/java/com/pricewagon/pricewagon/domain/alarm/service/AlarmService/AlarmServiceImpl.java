package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.pricewagon.pricewagon.domain.alarm.dto.AlarmRequestDTO;
import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.alarm.entity.AlarmStatus;
import com.pricewagon.pricewagon.domain.alarm.repository.AlarmRepository;
import com.pricewagon.pricewagon.domain.alarm.repository.SseRepository;
import com.pricewagon.pricewagon.domain.fcm.entity.FcmToken;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepository;
import com.pricewagon.pricewagon.domain.product.service.ProductService;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmServiceImpl implements AlarmService {
	private final static Long DEFAULT_TIMEOUT = 3600000L;
	private final static String NOTIFICATION_NAME = "notify";
	private final ProductService productService;
	private final UserRepository userRepository;
	private final AlarmRepository alarmRepository;
	private final SseRepository sseRepository;
	private final ProductRepository productRepository;
	private final FirebaseMessaging firebaseMessaging;

	// @Override
	// public SseEmitter subscribe(String email, String lastEventId) {
	// 	String emitterId = email + "_" + System.currentTimeMillis();
	//
	// 	SseEmitter sseEmitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
	// 	log.info("new emitter added : {}", sseEmitter);
	// 	log.info("lastEventId : {}", lastEventId);
	//
	// 	sseEmitter.onCompletion(() -> sseRepository.deleteEmitterById(emitterId));
	// 	sseEmitter.onTimeout(() -> sseRepository.deleteEmitterById(emitterId));
	// 	sseEmitter.onError((e) -> sseRepository.deleteEmitterById(emitterId));
	//
	// 	Alarm dummyNotification = createDummyNotification(email);
	// 	emitEventToClient(sseEmitter, emitterId, dummyNotification);
	//
	// 	if (!lastEventId.isEmpty()) {
	// 		Map<String, Object> eventCaches = sseRepository.findAllEventCacheStartsWithUsername(email);
	// 		eventCaches.entrySet().stream()
	// 			.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
	// 			.forEach(entry -> emitEventToClient(sseEmitter, entry.getKey(), entry.getValue()));
	// 	}
	//
	// 	return sseEmitter;
	// }
	//
	// @Override
	// public void send(String receiver, String content, String type, String url) {
	// 	Alarm alarm = createNotification(receiver, content, type, url);
	// 	Map<String, SseEmitter> sseEmitters = sseRepository.findAllEmitterStartsWithUsername(receiver);
	// 	sseEmitters.forEach((key, sseEmitter) -> {
	// 		log.info("key, notification : {}, {}", key, alarm);
	// 		sseRepository.saveEventCache(key, alarm);
	// 		emitEventToClient(sseEmitter, key, alarm);
	// 	});
	// }

	@Async
	@Scheduled(fixedRate = 60000) // Every 1 minute
	public void checkAllAlarmsAndNotify() {
		List<Alarm> activeAlarms = alarmRepository.findByStatus(AlarmStatus.ACTIVE);

		for (Alarm alarm : activeAlarms) {
			if (productService.isPriceBelowDesired(alarm)) {
				send(
					alarm.getUser().getAccount(),
					alarm.getProduct().getName() + "의 가격이 " + alarm.getDesired_price() + "원 이하로 떨어졌습니다.",
					"PRICE_ALERT",
					alarm.getUrl()
				);
				alarm.setStatus(AlarmStatus.INACTIVE);
				alarmRepository.save(alarm);
				log.info("Notification sent and alarm status updated for alarmId: {}", alarm.getId());
			}
		}
	}

	@Override
	public void registerAlarm(AlarmRequestDTO.registerAlarm request, String username) {
		Product product = productRepository.findById(request.getProductId())
			.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		User user = userRepository.findByAccount(username)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
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
			.content("default")
			.desired_price(request.getPrice())
			.status(AlarmStatus.ACTIVE)
			.build();
		alarmRepository.save(alarm);
	}

	@Override
	public String sendAlarmByToken(AlarmRequestDTO.FCMAlarmRequestDTO request) {
		Optional<User> user = userRepository.findById(request.getUserId());
		if(user.isPresent()){
			if(user.get().getFcmTokens() != null){
				Alarm alarm = Alarm.builder()
					.user(user.get())
					.content(request.getBody())
					.status(AlarmStatus.ACTIVE)
					.build();
			}
			try{
				firebaseMessaging.send(message);
				return "알림을 성공적으로 전송했습니다. userId: "+request.getUserId();
			}catch(FirebaseMessagingException e){
				return "알림 전송에 실패했습니다. userId: "+request.getUserId();
			}else{
				return "해당 유저의 토큰이 없습니다. userId: "+request.getUserId();
			}
		}else{
			return "해당 유저가 존재하지 않습니다. userId: "+request.getUserId();


		}
	}

	// private Alarm createDummyNotification(String receiver) {
	// 	return Alarm.builder()
	// 		.content("Dummy notification")
	// 		.url("")
	// 		.status(AlarmStatus.ACTIVE)
	// 		.build();
	// }
	//
	// private Alarm createNotification(String receiver, String content, String type, String url) {
	// 	User user = userRepository.findByAccount(receiver)
	// 		.orElseThrow(() -> new IllegalArgumentException("User not found"));
	// 	return Alarm.builder()
	// 		.user(user)
	// 		.content(content)
	// 		.url(url)
	// 		.status(AlarmStatus.ACTIVE)
	// 		.build();
	// }
	//
	// private void emitEventToClient(SseEmitter sseEmitter, String key, Object data) {
	// 	try {
	// 		sseEmitter.send(SseEmitter.event().name(NOTIFICATION_NAME).id(key).data(data));
	// 	} catch (Exception e) {
	// 		sseRepository.deleteEmitterById(key);
	// 	}
	// }

}