package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

public record ProductAndBrandSearchResponse(
	List<String> relatedProducts,

	List<String> relatedBrands
) {
	public static ProductAndBrandSearchResponse of(List<String> relatedProducts, List<String> relatedBrands) {
		return new ProductAndBrandSearchResponse(relatedProducts, relatedBrands);
	}
}
