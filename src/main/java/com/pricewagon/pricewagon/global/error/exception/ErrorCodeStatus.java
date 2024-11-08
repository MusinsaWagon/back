package com.pricewagon.pricewagon.global.error.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCodeStatus {
	String name();

	HttpStatus getHttpStatus();

	String getMessage();
}
