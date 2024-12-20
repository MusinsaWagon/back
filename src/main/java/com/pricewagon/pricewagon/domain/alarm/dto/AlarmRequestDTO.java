package com.pricewagon.pricewagon.domain.alarm.dto;

import java.time.LocalDateTime;

import lombok.Getter;

public class AlarmRequestDTO {
	@Getter
	public static class registerAlarm {
		Integer price;
		Integer productId;
		LocalDateTime endDate;
		String fcmToken;
	}

}
