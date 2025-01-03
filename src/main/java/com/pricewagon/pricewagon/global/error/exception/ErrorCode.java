package com.pricewagon.pricewagon.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ErrorCodeStatus {
	// 서버 오류
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP method 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류, 관리자에게 문의하세요"),
	METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Enum Type이 일치하지 않거나 값이 없습니다."),
	REQUESTED_PARAM_NOT_VALIDATE(HttpStatus.BAD_REQUEST, "Reqeust Param 값이 유효하지 않습니다"),
	REQUESTED_VALUE_NOT_VALIDATE(HttpStatus.BAD_REQUEST, "메서드의 인자가 유효하지 않습니다."),
	NOT_FOUND_REQUEST_ADDRESS(HttpStatus.NOT_FOUND, "잘못된 요청 주소입니다."),
	NOT_FOUND_REQUEST_RESOURCE(HttpStatus.NOT_FOUND, "존재하지 않은 요청 주소입니다."),

	//멤버 관련 오류
	MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "멤버가 이미 존재합니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수  없습니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

	//상품 관련 오류
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

	//좋아요 관련 오류
	ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 상품입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
