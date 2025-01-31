package com.pricewagon.pricewagon.domain.product.api;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.product.dto.request.ProductUrlRequest;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo2;
import com.pricewagon.pricewagon.domain.product.dto.response.KeywordAndBrandSearchResponse;
import com.pricewagon.pricewagon.domain.product.dto.response.SearchingBrandResponse;
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

	@Operation(summary = "개별 상품 정보 조회", description = "특정 상품에 대한 정보")
	@GetMapping("/{shopType}/{productNumber}")
	public IndividualProductInfo getIndividualProductInfo(
		@PathVariable ShopType shopType,
		@PathVariable Integer productNumber,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		if (userDetails != null) {
			return productService.getIndividualProductInfo(shopType, productNumber, userDetails);
		} else {
			return productService.getIndividualProductInfo(shopType, productNumber, null);
		}
	}

	@Operation(summary = "개별 상품 정보 조회 옛 버전", description = "이건 사용 안하는 API 입니다. 포폴 업데이트 후 삭제 예정")
	@GetMapping("/content/{shopType}/{productNumber}")
	public IndividualProductInfo2 getIndividualProductInfo2(
		@PathVariable ShopType shopType,
		@PathVariable Integer productNumber
	) {
		return productService.getIndividualProductInfo2(shopType, productNumber);
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

	@Operation(summary = "브랜드 검색", description = "브랜드 검색하기")
	@GetMapping("/{shopType}/search/brand")
	public SearchingBrandResponse searchBrands(
		@PathVariable ShopType shopType,
		@RequestParam(required = true) String name,
		@RequestParam(defaultValue = "10") int size
	) {
		List<String> brandsList = productService.searchBrandsName(shopType, name, size);
		return SearchingBrandResponse.from(brandsList);
	}

	@Operation(summary = "검색하기", description = "상품명, 브랜드 동시 검색")
	@GetMapping("/{shopType}/search")
	public KeywordAndBrandSearchResponse searchProductsAndBrands(
		@PathVariable ShopType shopType,
		@RequestParam(required = true) String name,
		@RequestParam(defaultValue = "10") int size
	) {
		return productService.searchKeywordsAndBrands(shopType, name, size);
	}

	@Operation(summary = "검색 후 관련 상품 및 브랜드 정보 요청", description = "검색 후 관련 상품 및 브랜드 세부 정보 조회")
	@GetMapping("/{shopType}/search/detail")
	public List<BasicProductInfo> searchProductsAndBrandsDetail(
		@PathVariable ShopType shopType,
		@RequestParam(required = false) Integer lastId,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String brand
	) {
		return productService.getSearchingProductsAndBrandsDetail(shopType, keyword, brand, lastId, size);
	}

	@Operation(summary = "좋아요 기준 인기 상품 조회", description = "좋아요 많이 등록한  기준 인기 상품 조회")
	@GetMapping("/popular/{shopType}")
	public List<BasicProductInfo> getPopularProducts(
		@PathVariable ShopType shopType,
		@RequestParam(required = false) Integer lastId,
		@RequestParam(defaultValue = "10") int size
	) {
		return productService.getPopularProducts(shopType, lastId, size);
	}

	@Operation(summary = "좋아요 기준 인기 상품 조회", description = "좋아요 많이 등록한  기준 인기 상품 조회")
	@GetMapping("/popular/{shopType}")
	public List<BasicProductInfo> getPopularProducts(
		@PathVariable ShopType shopType,
		@RequestParam(required = false) Integer lastId,
		@RequestParam(defaultValue = "10") int size
	) {
		return productService.getPopularProducts(shopType, lastId, size);
	}

	@Operation(summary = "알람 기준 인기 상품 조회", description = "알람 많이 등록한  기준 인기 상품 조회")
	@GetMapping("/alarm/{shopType}")
	public List<BasicProductInfo> getAlarmProducts(
		@PathVariable ShopType shopType,
		@RequestParam(required = false) Integer lastId,
		@RequestParam(defaultValue = "10") int size
	) {
		return productService.getPopularProductsByAlarm(shopType, lastId, size);
	}

}
