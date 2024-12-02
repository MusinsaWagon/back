package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.pricewagon.pricewagon.domain.user.entity.User;

public interface AlarmService {
	SseEmitter subscribe(Long userId);

	<T> void customNotify(Long userId, T data, String comment, String type);

	void notify(Long userId, Object data, String comment);

	void sendToClient(Long userId, Object data, String comment);

	<T> void sendToClient(Long userId, T data, String comment, String type);

	SseEmitter createEmitter(Long userId);

	User validUser(Long userId);

}
