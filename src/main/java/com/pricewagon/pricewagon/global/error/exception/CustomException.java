package com.pricewagon.pricewagon.global.error.exception;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException implements Serializable {

	private final transient ErrorCodeStatus errorCodeStatus;

	public CustomException(ErrorCodeStatus errorCodeStatus) {
		super(errorCodeStatus.getMessage());
		this.errorCodeStatus = errorCodeStatus;
	}
}