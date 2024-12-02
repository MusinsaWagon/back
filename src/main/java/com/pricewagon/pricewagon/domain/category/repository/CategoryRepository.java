package com.pricewagon.pricewagon.domain.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pricewagon.pricewagon.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByParentCategory_Id(Long parentCateghoryId);

	@Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
	List<Category> findParentCategories();
}
