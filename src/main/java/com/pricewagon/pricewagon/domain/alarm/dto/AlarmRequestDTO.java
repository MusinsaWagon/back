package com.pricewagon.pricewagon.domain.alarm.dto;

import lombok.Getter;

public class AlarmRequestDTO {
	@Getter
	public static class registerAlarm {
		Integer price;
		Integer productId;
	}

	@Getter
	public static class sendAlarmDTO {
		String token;
		String title;
		String body;
	}

	@Getter
	public static class FCMAlarmRequestDTO {
		Long userId;
		String title;
		String body;
	}

}
