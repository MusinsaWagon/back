package com.pricewagon.pricewagon.domain.category.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.category.dto.CategoryDTO;
import com.pricewagon.pricewagon.domain.category.dto.response.AllCategoryResponse;
import com.pricewagon.pricewagon.domain.category.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "2. [카테고리]", description = "카테고리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
	private final CategoryService categoryService;

	@Operation(summary = "부모 카테고리로 하위 카테고리 조회", description = "부모와 하위 카테고리 포함해 반환")
	@GetMapping("/{categoryId}")
	public AllCategoryResponse getParentAndSubCategories(
		@PathVariable Long categoryId
	) {
		return categoryService.getParentAndSubCategoriesByParentId(categoryId);
	}

	@Operation(summary = "부모 카테고리 조회", description = "쇼핑몰별 부모 카테고리만 반환")
	@GetMapping("/parents")
	public List<CategoryDTO> getParentCategories(
	) {
		return categoryService.getParentCategories();
	}
}
