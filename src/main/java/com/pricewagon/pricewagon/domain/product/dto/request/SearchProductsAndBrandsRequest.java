package com.pricewagon.pricewagon.domain.product.dto.request;

public record SearchProductsAndBrandsRequest(
	String brand,
	String keyword
) {

}
