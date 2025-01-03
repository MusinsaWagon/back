package com.pricewagon.pricewagon.domain.likes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.likes.entity.Like;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.user.entity.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
	boolean existsByUserAndProduct(User user, Product product);

	Optional<Like> findByUserAndProduct(User user, Product product);
}
