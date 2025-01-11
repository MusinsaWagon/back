package com.pricewagon.pricewagon.domain.product.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;

public final class ProductSpecification {

	private ProductSpecification() {
		throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
	}

	public static Specification<Product> hasName(String name) {
		return (root, query, builder) -> {
			if (name == null || name.isBlank()) {
				return builder.disjunction();
			}
			return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
		};
	}

	public static Specification<Product> hasBrand(String brand) {
		return (root, query, builder) -> {
			if (brand == null || brand.isBlank()) {
				return builder.disjunction();
			}
			return builder.like(builder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
		};
	}

	public static Specification<Product> equalShopType(ShopType shopType) {
		return (root, query, builder) ->
			builder.equal(root.get("shopType"), shopType);
	}

	public static Specification<Product> gtProductId(Integer lastId) {
		return (root, query, builder) -> {
			if (lastId == null) {
				return builder.conjunction();
			}
			return builder.greaterThan(root.get("id"), lastId);
		};
	}
}
