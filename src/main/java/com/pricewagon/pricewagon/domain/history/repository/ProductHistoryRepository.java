package com.pricewagon.pricewagon.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.history.entity.ProductHistory;

public interface ProductHistoryRepository
	extends JpaRepository<ProductHistory, Integer>, ProductHistoryCustomRepository {
}

