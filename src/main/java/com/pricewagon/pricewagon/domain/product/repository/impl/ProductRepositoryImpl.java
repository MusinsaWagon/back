package com.pricewagon.pricewagon.domain.product.repository.impl;

import static com.pricewagon.pricewagon.domain.product.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Product> findProductsByShopTypeAndLastId(ShopType shopType, Integer lastId, int size) {
		// QueryDSL을 사용하여 동적 쿼리 작성 및 실행
		return jpaQueryFactory
			.selectFrom(product)
			.where(gtProductId(lastId))
			.orderBy(product.id.asc()) // id 기준 오름차순 정렬
			.limit(size) // 페이지 크기 설정
			.fetch();
	}

	@Override
	public List<Product> findProductsByQueryAndLastId(String name, Integer lastId, int size) {
		// QueryDSL 필터링 적용
		BooleanExpression conditions = createSearchCondition(name);

		return jpaQueryFactory
			.selectFrom(product)
			.where(
				gtProductId(lastId),
				conditions
			)
			.orderBy(product.id.asc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<Product> findPopularProductsByShopTypeAndLastId(ShopType shopType, Integer lastId, int size) {
		return jpaQueryFactory
			.selectFrom(product)
			.where(product.shopType.eq(shopType),
				lastId != null ? product.id.lt(lastId) : null)
			.orderBy(product.userLikeCount.desc(), product.id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<Product> findAlarmProductsByShopTypeAndLastId(ShopType shopType, Integer lastId, int size) {
		return jpaQueryFactory
			.selectFrom(product)
			.where(product.shopType.eq(shopType),
				lastId != null ? product.id.lt(lastId) : null)
			.orderBy(product.alarmCount.desc(), product.id.desc())
			.limit(size)
			.fetch();
	}

	private BooleanExpression createSearchCondition(String query) {
		if (query == null || query.isBlank()) {
			return null;
		}

		// 검색어를 이름과 브랜드 모두에 적용
		return product.name.containsIgnoreCase(query)
			.or(product.brand.containsIgnoreCase(query));
	}

	private BooleanExpression gtProductId(Integer lastProductId) {
		return lastProductId == null ? null : product.id.gt(lastProductId);
	}

}
