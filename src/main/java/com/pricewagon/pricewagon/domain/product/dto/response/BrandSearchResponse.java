package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

public record BrandSearchResponse(
	List<String> brands
) {
	public static BrandSearchResponse of(List<String> brands) {
		return new BrandSearchResponse(brands);
	}
}
