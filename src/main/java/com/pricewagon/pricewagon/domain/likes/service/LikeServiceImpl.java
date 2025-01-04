package com.pricewagon.pricewagon.domain.likes.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pricewagon.pricewagon.domain.likes.dto.LikeResponseDTO;
import com.pricewagon.pricewagon.domain.likes.entity.Likes;
import com.pricewagon.pricewagon.domain.likes.repository.LikeRepository;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepository;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;
import com.pricewagon.pricewagon.global.error.exception.CustomException;
import com.pricewagon.pricewagon.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
	private final LikeRepository likeRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	@Override
	public LikeResponseDTO.registerLikeDTO registerLike(Integer productNumber, String username) {
		User user = userRepository.findByAccount(username)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Product product = productRepository.findByProductNumber(productNumber)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		Optional<Likes> like = likeRepository.findByUserAndProduct(user, product);
		if (like.isPresent()) { // 좋아요 삭제
			likeRepository.delete(like.get());
			product.updateLikeCount(product.getLikeCount() - 1);
			return LikeResponseDTO.registerLikeDTO.builder()
				.productId(productNumber)
				.userId(user.getId())
				.action("좋아요 삭제")
				.build();
		} else {
			Likes newLike = Likes.builder()
				.user(user)
				.product(product)
				.likedAt(LocalDateTime.now())
				.build();
			likeRepository.save(newLike);
			product.updateLikeCount(product.getLikeCount() + 1);
			return LikeResponseDTO.registerLikeDTO.builder()
				.productId(productNumber)
				.userId(user.getId())
				.action("좋아요 등록")
				.build();
		}

	}

}
