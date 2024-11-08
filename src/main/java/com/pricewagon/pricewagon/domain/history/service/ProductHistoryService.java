package com.pricewagon.pricewagon.domain.history.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.history.entity.ProductHistory;
import com.pricewagon.pricewagon.domain.history.repository.ProductHistoryRepository;
import com.pricewagon.pricewagon.domain.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductHistoryService {

	private final ProductHistoryRepository productHistoryRepository;

	// 현개 가격과 다른 첫 번째 값을 찾고, 없으면 최근 값을 리턴
	public Integer getDifferentLatestPriceByProductId(Product product) {

		ProductHistory previousHistory = productHistoryRepository.findFirstByProductIdAndPriceNot(product.getId(),
				product.getCurrentPrice())
			.orElse(null);

		if (previousHistory == null)
			return product.getCurrentPrice();

		return previousHistory.getPrice();
	}
}
