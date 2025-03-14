package com.pricewagon.pricewagon.domain.history.repository.impl;

import static com.pricewagon.pricewagon.domain.history.entity.QProductHistory.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.pricewagon.pricewagon.domain.history.entity.ProductHistory;
import com.pricewagon.pricewagon.domain.history.repository.ProductHistoryCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductHistoryRepositoryImpl implements ProductHistoryCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<ProductHistory> findFirstByProductIdAndPriceNot(Long productId, Integer currentPrice) {
		ProductHistory result = jpaQueryFactory.selectFrom(productHistory)
			.where(productHistory.product.id.eq(productId)
				.and(productHistory.price.ne(currentPrice)))
			.orderBy(productHistory.createdAt.desc())
			.fetchFirst();

		return Optional.ofNullable(result);
	}
}
