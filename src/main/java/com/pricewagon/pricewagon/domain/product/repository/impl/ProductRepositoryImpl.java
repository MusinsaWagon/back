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
		return jpaQueryFactory
			.selectFrom(product)
			.where(
				gtProductId(lastId),
				eqShopType(shopType)
			)
			.orderBy(product.id.asc()) // id 기준 오름차순 정렬
			.limit(size) // 페이지 크기 설정
			.fetch();
	}

	@Override
	public List<Product> findProductsByQueryAndLastId(String name, Integer lastId, int size) {

		return jpaQueryFactory
			.selectFrom(product)
			.where(
				gtProductId(lastId),
				createSearchCondition(name)
			)
			.orderBy(product.id.asc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<Product> findPopularProductsByShopTypeAndLastId(ShopType shopType, Integer lastId, int size) {
		return jpaQueryFactory
			.selectFrom(product)
			.where(eqShopType(shopType),
				gtProductId(lastId))
			.orderBy(product.userLikeCount.desc(), product.id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<Product> findAlarmProductsByShopTypeAndLastId(ShopType shopType, Integer lastId, int size) {
		return jpaQueryFactory
			.selectFrom(product)
			.where(
				eqShopType(shopType),
				gtProductId(lastId))
			.orderBy(product.alarmCount.desc(), product.id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<Product> findSearchingProductsAndBrands(ShopType shopType, String brand, String keyword,
		Integer lastId, int size) {
		return jpaQueryFactory
			.selectFrom(product)
			.where(
				gtProductId(lastId),
				cotainsIgnoreBrand(brand),
				containsIgnoreName(keyword)
			)
			.orderBy(product.id.asc()) // id 기준 오름차순 정렬
			.limit(size) // 페이지 크기 설정
			.fetch();
	}

	private BooleanExpression cotainsIgnoreBrand(String brand) {
		if (brand == null || brand.isBlank()) {
			return null;
		}
		return (product.brand.containsIgnoreCase(brand));
	}

	private BooleanExpression containsIgnoreName(String name) {
		if (name == null || name.isBlank()) {
			return null;
		}
		return (product.name.containsIgnoreCase(name));
	}

	private BooleanExpression createSearchCondition(String query) {
		if (query == null || query.isBlank()) {
			return null;
		}

		// 검색어를 이름과 브랜드 모두에 적용
		return product.name.containsIgnoreCase(query)
			.or(product.brand.containsIgnoreCase(query));
	}

	private BooleanExpression eqShopType(ShopType shopType) {
		return product.shopType.eq(shopType);
	}

	private BooleanExpression gtProductId(Integer lastProductId) {
		return lastProductId == null ? null : product.id.gt(lastProductId);
	}

}
