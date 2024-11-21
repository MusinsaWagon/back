package com.pricewagon.pricewagon.domain.history.repository;

import java.util.Optional;

import com.pricewagon.pricewagon.domain.history.entity.ProductHistory;

public interface ProductHistoryCustomRepository {
	Optional<ProductHistory> findFirstByProductIdAndPriceNot(Long productId, Integer currentPrice);
}
