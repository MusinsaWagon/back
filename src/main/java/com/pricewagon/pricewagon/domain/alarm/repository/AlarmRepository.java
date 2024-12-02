package com.pricewagon.pricewagon.domain.alarm.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class AlarmRepository {
	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

	public SseEmitter findById(Long userId) {
		return emitters.get(userId);
	}
}
