package com.pricewagon.pricewagon.domain.product.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
	SUCCESS("성공"),
	FAIL("실패"),
	PENDING("대기");
	private final String value;
}
