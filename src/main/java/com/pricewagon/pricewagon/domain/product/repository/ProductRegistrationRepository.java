package com.pricewagon.pricewagon.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.ProductRegistration;

public interface ProductRegistrationRepository extends JpaRepository<ProductRegistration, Long> {
}
