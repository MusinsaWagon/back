package com.pricewagon.pricewagon.domain.product.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pricewagon.pricewagon.domain.product.entity.Product;

public final class ProductSpecification {

	private ProductSpecification() {
		throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
	}

	public static Specification<Product> hasName(String name) {
		return (root, query, builder) ->
			name == null ? null : builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}

	public static Specification<Product> hasBrand(String brand) {
		return (root, query, builder) ->
			brand == null ? null : builder.like(builder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
	}
}
