package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

public record KeywordAndBrandSearchResponse(
	List<String> relatedBrands,
	List<String> relatedKeywords

) {
	public static KeywordAndBrandSearchResponse of(List<String> relatedBrands, List<String> relatedKeywords) {
		return new KeywordAndBrandSearchResponse(relatedBrands, relatedKeywords);
	}
}
