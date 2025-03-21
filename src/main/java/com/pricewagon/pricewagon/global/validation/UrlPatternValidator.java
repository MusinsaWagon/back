package com.pricewagon.pricewagon.global.validation;

import com.pricewagon.pricewagon.domain.product.dto.request.ProductUrlRequest;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UrlPatternValidator implements ConstraintValidator<ValidUrlPattern, ProductUrlRequest> {
	@Override
	public boolean isValid(ProductUrlRequest value, ConstraintValidatorContext context) {
		if (value == null || value.url() == null || value.shopType() == null) {
			return false;
		}
		String url = value.url();
		ShopType shopType = value.shopType();
		String regex = "";

		// 쇼핑몰별 URL 패턴 설정 (숫자 6~10자리)
		switch (shopType) {
			case MUSINSA:
				regex = "^https://www\\.musinsa\\.com/products/\\d{6,10}$";
				break;
			case ABLY:
				regex = "^https://m\\.a-bly\\.com/goods/\\d{6,10}$";
				break;
			case BRANDI:
				regex = "^https://www\\.brandi\\.co\\.kr/products/\\d{6,10}$";
				break;
			case ZIGZAG:
				regex = "^https://zigzag\\.kr/catalog/\\d{6,10}$";
				break;
			default:
				return false;
		}

		// 정규 표현식을 사용하여 URL 형식이 유효한지 검사
		return url.matches(regex);
	}
}
