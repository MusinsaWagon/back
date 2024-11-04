package com.pricewagon.pricewagon.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.type.Shop;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
	Page<Product> findAllByShop(Shop shop, Pageable pageable);

	Optional<Product> findByShopAndProductNumber(Shop shop, Integer productNumber);

	Page<Product> findByShopAndCategory_IdIn(Shop shop, List<Integer> categoryIds, Pageable pageable);

	boolean existsByProductNumber(Integer productNumber);

}
