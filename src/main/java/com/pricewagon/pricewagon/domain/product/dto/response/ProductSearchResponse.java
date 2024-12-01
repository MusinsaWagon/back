package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

import com.pricewagon.pricewagon.domain.product.dto.ProductDto;

public record ProductSearchResponse(
	List<ProductDto> relatedProducts
) {
	public static ProductSearchResponse of(List<ProductDto> relatedProducts) {

		return new ProductSearchResponse(relatedProducts);
	}
}
