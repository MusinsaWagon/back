package com.pricewagon.pricewagon.domain.alarm.dto;

import lombok.Getter;

public class AlarmRequestDTO {
	@Getter
	public static class registerAlarm {
		Integer price;
		Integer productId;
		String fcmToken;
	}

}
