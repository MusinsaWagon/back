package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

import com.pricewagon.pricewagon.domain.category.dto.response.ParentAndChildCategoryDTO;
import com.pricewagon.pricewagon.domain.history.entity.ProductHistory;
import com.pricewagon.pricewagon.domain.product.dto.ProductDetailDto;
import com.pricewagon.pricewagon.domain.product.entity.Product;

public record IndividualProductInfo2(

	BasicProductInfo basicProductInfo, // 메인 정보
	ProductDetailDto productDetail, // 부과 정보
	List<ProductHistory> productHistoryList, // 가격 히스토리
	ParentAndChildCategoryDTO parentAndChildCategoryDTO // 카테고리
) {

	public static IndividualProductInfo2 from(Product product, BasicProductInfo basicProductInfo,
		ParentAndChildCategoryDTO parentAndChildCategoryDTO) {
		ProductDetailDto productDetail = ProductDetailDto.toDTO(product.getProductDetail());
		return new IndividualProductInfo2(basicProductInfo, productDetail, product.getProductHistories(),
			parentAndChildCategoryDTO);
	}
}
