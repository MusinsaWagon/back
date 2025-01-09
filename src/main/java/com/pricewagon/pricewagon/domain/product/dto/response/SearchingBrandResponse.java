package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

public record SearchingBrandResponse(
	List<String> relatedBrands
) {
	public static SearchingBrandResponse from(List<String> relatedBrands) {
		return new SearchingBrandResponse(relatedBrands);
	}
}
