package com.pricewagon.pricewagon.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AlarmResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class registerAlarmDTO {
		Integer price;
		Integer productId;
		String account;
	}
}
