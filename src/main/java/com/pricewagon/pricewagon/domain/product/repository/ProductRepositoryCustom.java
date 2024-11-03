package com.pricewagon.pricewagon.domain.product.repository;

import java.util.List;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.type.Shop;

public interface ProductRepositoryCustom {
	List<Product> findProductsByShopAndLastId(Shop shop, Integer lastId, int size);
}
