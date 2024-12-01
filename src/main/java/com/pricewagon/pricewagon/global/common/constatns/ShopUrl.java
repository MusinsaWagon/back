package com.pricewagon.pricewagon.global.common.constatns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopUrl {
	MUSINSA("https://www.musinsa.com/products/"),
	ABLY("https://m.a-bly.com/goods/"),
	BRANDI("https://www.brandi.co.kr/products/"),
	ZIGZAG("https://zigzag.kr/catalog/");
	private String value;
}
