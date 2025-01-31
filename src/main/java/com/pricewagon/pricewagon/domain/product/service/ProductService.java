package com.pricewagon.pricewagon.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.category.dto.response.ParentAndChildCategoryDTO;
import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.category.service.CategoryService;
import com.pricewagon.pricewagon.domain.history.service.ProductHistoryService;
import com.pricewagon.pricewagon.domain.likes.repository.LikeRepository;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo2;
import com.pricewagon.pricewagon.domain.product.dto.response.KeywordAndBrandSearchResponse;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepository;
import com.pricewagon.pricewagon.domain.product.specification.ProductSpecification;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;
import com.pricewagon.pricewagon.global.error.exception.CustomException;
import com.pricewagon.pricewagon.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductHistoryService productHistoryService;
	private final CategoryService categoryService;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;

	// 쇼핑몰에 따른 상품 리스트 조회
	@Transactional(readOnly = true)
	public List<BasicProductInfo> getProductsByShopType(ShopType shopType, Integer lastId, int size) {
		List<Product> products = productRepository.findProductsByShopTypeAndLastId(shopType, lastId, size);
		return convertToBasciProductInfo(products);
	}

	// 검색 후 상품 및 브랜드 리스트 조회
	@Transactional(readOnly = true)
	public List<BasicProductInfo> getSearchingProductsAndBrandsDetail(ShopType shopType,
		String keyword, String brand, Integer lastId,
		int size) {

		if (brand != null && !brand.isBlank() && (keyword == null || keyword.isBlank())) {
			List<Product> relatedProducts = searchProductByBrands(shopType, brand, size);
			return convertToBasciProductInfo(relatedProducts);
		}

		List<Product> relatedProducts = productRepository.findSearchingProductsAndBrands(shopType, brand, keyword,
			lastId,
			size);
		return convertToBasciProductInfo(relatedProducts);
	}

	// 좋아요 기반으로 인기 상품 조회
	@Transactional(readOnly = true)
	public List<BasicProductInfo> getPopularProducts(ShopType shopType, Integer lastId, int size) {
		List<Product> popularProducts = productRepository.findPopularProductsByShopTypeAndLastId(shopType, lastId,
			size);
		return convertToBasciProductInfo(popularProducts);
	}

	// 알람 기반으로 인기 상품 조회
	@Transactional(readOnly = true)
	public List<BasicProductInfo> getPopularProductsByAlarm(ShopType shopType, Integer lastId, int size) {
		List<Product> popularProducts = productRepository.findAlarmProductsByShopTypeAndLastId(shopType, lastId,
			size);
		return convertToBasciProductInfo(popularProducts);

	}

	// 개별 상품 정보 조회
	@Transactional(readOnly = true)
	public IndividualProductInfo getIndividualProductInfo(ShopType shopType, Integer productNumber,
		UserDetails userDetails) {
		Product product = productRepository.findByShopTypeAndProductNumber(shopType, productNumber)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

		Category childCategory = product.getCategory();
		ParentAndChildCategoryDTO parentAndChildCategoryDTO = categoryService
			.getParentAndChildCategoryByChildId(childCategory.getId());

		Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);

		boolean isLiked = false;
		if (userDetails != null) {
			User user = userRepository.findByAccount(userDetails.getUsername())
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
			isLiked = likeRepository.existsByUserAndProduct(user, product);
		}
		BasicProductInfo basicProductInfo = BasicProductInfo.createWithLikeStatus(product, previousPrice, isLiked);

		return IndividualProductInfo.from(
			product,
			basicProductInfo,
			parentAndChildCategoryDTO
		);
	}

	// 개별 상품 정보 조회
	@Transactional(readOnly = true)
	public IndividualProductInfo2 getIndividualProductInfo2(ShopType shopType, Integer productNumber) {
		Product product = productRepository.findByShopTypeAndProductNumber(shopType, productNumber)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

		Category childCategory = product.getCategory();
		ParentAndChildCategoryDTO parentAndChildCategoryDTO = categoryService
			.getParentAndChildCategoryByChildId(childCategory.getId());

		Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);
		BasicProductInfo basicProductInfo = BasicProductInfo.createHistoryOf(product, previousPrice);

		return IndividualProductInfo2.from(
			product,
			basicProductInfo,
			parentAndChildCategoryDTO
		);
	}

	// 카테고리, 쇼핑몰, 페이지 수로 상품 리스트 조회
	public List<BasicProductInfo> getBasicProductsByCategory(ShopType shopType, Pageable pageable,
		Long parentCategoryId) {
		Page<Product> productPage;

		// 전체 상품 조회 categoryId = 0
		if (parentCategoryId == 0) {
			productPage = productRepository.findAllByShopType(shopType, pageable);
		} else {
			// parentCategoryId에 대한 상품 조회
			List<Long> categoriesId = categoryService.getAllCategoryIds(parentCategoryId);
			productPage = productRepository.findByShopTypeAndCategory_IdIn(shopType, categoriesId, pageable);
		}

		return productPage.stream()
			.map(this::convertToBasicProductInfo)
			.toList();
	}

	// 변환 로직 분리
	private BasicProductInfo convertToBasicProductInfo(Product product) {
		Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);
		return BasicProductInfo.createHistoryOf(product, previousPrice);
	}

	@Transactional(readOnly = true)
	public KeywordAndBrandSearchResponse searchKeywordsAndBrands(ShopType shopType, String name, int size) {
		List<String> relatedKeywords = searchProductNames(shopType, name, size);
		List<String> relatedBrands = searchBrandsName(shopType, name, size);

		return KeywordAndBrandSearchResponse.of(relatedBrands, relatedKeywords);
	}

	public List<String> searchBrandsName(ShopType shopType, String brand, int size) {
		List<Product> productList = searchProductByBrands(shopType, brand, size);
		return productList.stream()
			.map(Product::getBrand)
			.distinct()
			.toList();
	}

	private List<Product> searchProductByBrands(ShopType shopType, String brand, int size) {
		Specification<Product> brandSpec = Specification.where(ProductSpecification.hasBrand(brand))
			.and(ProductSpecification.equalShopType(shopType));

		List<Product> productList = productRepository.findAll(brandSpec);

		return productList.stream()
			.distinct()
			.limit(size)
			.toList();
	}

	private List<Product> searchProductsByName(ShopType shopType, String name, int size) {
		Specification<Product> productNameSpec = Specification.where(
			ProductSpecification.hasName(name)
		).and(ProductSpecification.equalShopType(shopType));
		List<Product> productList = productRepository.findAll(productNameSpec);
		return productList.stream()
			.limit(size)
			.toList();
	}

	private List<String> searchProductNames(ShopType shopType, String name, int size) {
		List<Product> searchProducts = searchProductsByName(shopType, name, size);
		return searchProducts.stream()
			.map(Product::getBrand)
			.toList();
	}

	public boolean isPriceBelowDesired(Alarm alarm) {

		Integer currentPrice = productRepository.findById(alarm.getProduct().getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
			.getCurrentPrice();
		return currentPrice <= alarm.getDesired_price();
	}

	private List<BasicProductInfo> convertToBasciProductInfo(List<Product> products) {
		return products.stream()
			.map(product -> {
				Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);
				return BasicProductInfo.createHistoryOf(product, previousPrice);
			})
			.toList();
	}

}
