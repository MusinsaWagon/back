package com.pricewagon.pricewagon.domain.category.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.category.dto.CategoryDTO;
import com.pricewagon.pricewagon.domain.category.dto.response.AllCategoryResponse;
import com.pricewagon.pricewagon.domain.category.dto.response.ParentAndChildCategoryDTO;
import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public Category getCategoryById(Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new RuntimeException("해당 ID의 카테고리가 없습니다"));
	}

	// 부모 카테고리로 부모와 하위 카테고리 모두 리턴
	public AllCategoryResponse getParentAndSubCategoriesByParentId(Long parentCategoryId) {
		Category parentCategory = getCategoryById(parentCategoryId);

		List<Category> categories = getSubCategoriesByParentId(parentCategoryId);
		List<CategoryDTO> categoryDTOList = categories.stream()
			.map(CategoryDTO::toDTO)
			.toList();

		return AllCategoryResponse.toDTO(parentCategory, categoryDTOList);
	}

	//부모 ID로 하위 카테고리 찾기
	public List<Category> getSubCategoriesByParentId(Long parentCategoryId) {
		return categoryRepository.findAllByParentCategory_Id(parentCategoryId);
	}

	// 자식 ID로 부모, 자식 카테고리 리턴
	public ParentAndChildCategoryDTO getParentAndChildCategoryByChildId(Long childCategoryId) {
		Category childCategory = getCategoryById(childCategoryId);
		Category parentCategory = childCategory.getParentCategory();

		if (parentCategory == null) {
			throw new RuntimeException("해당 카테고리에는 부모 카테고리가 없습니다.");
		}
		return ParentAndChildCategoryDTO.from(parentCategory, childCategory);
	}

	// 부모 카테고리 id를 포함한 자식 카테고리 id의 리스트 반환
	public List<Long> getAllCategoryIds(Long parentCategoryId) {
		List<Category> childCategories = getSubCategoriesByParentId(parentCategoryId);

		// 부모 카테고리 ID와 자식 카테고리 ID를 하나의 리스트로 합침
		return Stream.concat(
			Stream.of(parentCategoryId),
			childCategories.stream().map(Category::getId)
		).toList();
	}
}
