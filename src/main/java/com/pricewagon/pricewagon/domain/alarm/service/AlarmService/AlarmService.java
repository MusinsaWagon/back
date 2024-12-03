package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.pricewagon.pricewagon.domain.alarm.dto.AlarmRequestDTO;

public interface AlarmService {
	SseEmitter subscribe(String email, String lastEventId);

	void send(String receiver, String content, String type, String url);

	void checkAllAlarmsAndNotify();

	void registerAlarm(AlarmRequestDTO.registerAlarm request, String email);

}
