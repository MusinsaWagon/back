package com.pricewagon.pricewagon.domain.product.api;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.product.dto.request.ProductUrlRequest;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.BrandSearchResponse;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.ProductSearchResponse;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;
import com.pricewagon.pricewagon.domain.product.service.ProductRegistrationService;
import com.pricewagon.pricewagon.domain.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. [상품]", description = "상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
	private final ProductService productService;
	private final ProductRegistrationService registrationService;

	@Operation(summary = "전체상품 페이지별 조회", description = "쇼핑몰별 모든 상품")
	@GetMapping("/{shopType}")
	public List<BasicProductInfo> getProductsByShopType(
		@PathVariable ShopType shopType,
		@RequestParam(required = false) Integer lastId,
		@RequestParam(defaultValue = "10") int size
	) {
		return productService.getProductsByShopType(shopType, lastId, size);
	}

	@Operation(summary = "개별 상품 정보 조최", description = "특정 상품에 대한 정보")
	@GetMapping("/{shopType}/{productNumber}")
	public IndividualProductInfo getIndividualProductInfo(
		@PathVariable ShopType shopType,
		@PathVariable Integer productNumber
	) {
		return productService.getIndividualProductInfo(shopType, productNumber);
	}

	@Operation(summary = "상위 카테고리에 속한 모든 상품조회", description = "카테고리에 속한 기본 상품정보")
	@GetMapping("/{shopType}/category/{categoryId}")
	public List<BasicProductInfo> getBasicProductsByCategory(
		@PathVariable ShopType shopType,
		@PathVariable Long categoryId,
		Pageable pageable
	) {
		return productService.getBasicProductsByCategory(shopType, pageable, categoryId);
	}

	@Operation(summary = "상품 등록", description = "크롤링 할 상품 URL 등록")
	@PostMapping("/registration")
	public void registerProductUrl(
		@Valid @RequestBody ProductUrlRequest request
	) {
		registrationService.registerProductURL(request);
	}

	@Operation(summary = "브랜드 이름 검색", description = "검색 시 브랜드 이름 검색")
	@GetMapping("/brands")
	public BrandSearchResponse searchBrands(
		@RequestParam(required = true) String name) {
		return productService.searchBrands(name);
	}

	@Operation(summary = "상품, 브랜드 검색", description = "상품명, 브랜드 동시 검색")
	@GetMapping("/search")
	public ProductSearchResponse searchProducts(
		@RequestParam(required = true) String name,
		@RequestParam(required = false) Integer lastId,
		@RequestParam(defaultValue = "10") int size
	) {
		return productService.searchProducts(name, lastId, size);
	}
	// @Operation(summary="좋아요 기준 인기 상품 조회", description="좋아요 많이 등록한  기준 인기 상품 조회")
	// @GetMapping("/popular")
	// public List<BasicProductInfo> getPopularProducts(
	// 	@RequestParam(defaultValue = "10") int size
	// ) {
	// 	return productService.getPopularProducts(size);
	// }
	//
	// @Operation(summary="알림 기준 인기 상품 조회", description="알림 많이 등록한 순서로 인기 상품 조회")

}
