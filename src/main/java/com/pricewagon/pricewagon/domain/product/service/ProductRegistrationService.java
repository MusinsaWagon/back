package com.pricewagon.pricewagon.domain.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.product.dto.request.ProductUrlRequest;
import com.pricewagon.pricewagon.domain.product.entity.ProductRegistration;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.ProductRegistrationRepository;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepository;
import com.pricewagon.pricewagon.global.common.constatns.ShopUrl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRegistrationService {

	private final ProductRegistrationRepository productRegistrationRepository;
	private final ProductRepository productRepository;

	// 상품 URL 등록
	public void registerProductURL(ProductUrlRequest request) {
		Integer productNumber = extractProductNumber(request.url(), request.shopType());

		if (productRepository.existsByProductNumber(productNumber)) {
			throw new RuntimeException("이미 등록 된 상품입니다.");
		}

		ProductRegistration productRegistration = ProductRegistration.create(request);
		productRegistrationRepository.save(productRegistration);
	}

	// 상품 url에서 상품번호 추출
	private Integer extractProductNumber(String url, ShopType shopType) {
		String baseUrl = getBaseUrl(shopType);
		return extractNumberFromUrl(url, baseUrl);
	}

	// 기본 URL을 제거하고 상품 번호를 추출
	private Integer extractNumberFromUrl(String url, String baseUrl) {
		String numberPart = url.substring(baseUrl.length());
		return Integer.parseInt(numberPart);
	}

	// shop 타입에 따른 기본 URL
	private String getBaseUrl(ShopType shopType) {
		return ShopUrl.valueOf(shopType.name()).getValue();
	}

}
