package com.pricewagon.pricewagon.domain.likes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.likes.entity.Likes;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.user.entity.User;

public interface LikeRepository extends JpaRepository<Likes, Long> {
	boolean existsByUserAndProduct(User user, Product product);

	Optional<Likes> findByUserAndProduct(User user, Product product);

	List<Likes> findByUserId(Long userId);
}
