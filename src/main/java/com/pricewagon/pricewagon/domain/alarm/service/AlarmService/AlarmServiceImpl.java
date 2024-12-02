package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.pricewagon.pricewagon.domain.alarm.repository.AlarmRepository;
import com.pricewagon.pricewagon.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmServiceImpl implements AlarmService {
	private final static Long DEFAULT_TIMEOUT = 3600000L;
	private final static String NOTIFICATION_NAME = "notify";
	private final AlarmRepository alarmRepository;

	@Override
	public SseEmitter subscribe(Long userId) {
		return null;
	}

	@Override
	public <T> void customNotify(Long userId, T data, String comment, String type) {

	}

	@Override
	public void notify(Long userId, Object data, String comment) {

	}

	@Override
	public void sendToClient(Long userId, Object data, String comment) {

	}

	@Override
	public <T> void sendToClient(Long userId, T data, String comment, String type) {

	}

	@Override
	public SseEmitter createEmitter(Long userId) {
		return null;
	}

	@Override
	public User validUser(Long userId) {
		return null;
	}
}
