package com.pricewagon.pricewagon.global.response;

import java.time.LocalDateTime;

import com.pricewagon.pricewagon.global.error.ErrorResponse;

public record GlobalResponse(boolean success, int status, Object data, LocalDateTime time) {
	public static GlobalResponse success(int status, Object data) {
		return new GlobalResponse(true, status, data, LocalDateTime.now().withNano(0));
	}

	public static GlobalResponse fail(int stauts, ErrorResponse errorResponse) {
		return new GlobalResponse(false, stauts, errorResponse, LocalDateTime.now().withNano(0));
	}
}