package com.pricewagon.pricewagon.domain.likes.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.pricewagon.pricewagon.domain.likes.dto.LikeResponseDTO;
import com.pricewagon.pricewagon.domain.likes.entity.Like;
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
	public LikeResponseDTO.registerLikeDTO registerLike(Long productId, String username) {
		User user = userRepository.findByAccount(username)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		boolean exists = likeRepository.existsByUserAndProduct(user, product);
		if (exists) {
			throw new CustomException(ErrorCode.ALREADY_LIKED);
		}

		Like like = Like.builder()
			.product(product)
			.user(user)
			.likedAt(LocalDateTime.now())
			.build();
		likeRepository.save(like);

		return LikeResponseDTO.registerLikeDTO.builder()
			.productId(productId)
			.userId(user.getId())
			.build();

	}

	@Override
	public LikeResponseDTO.registerLikeDTO cancelLike(Long productId, String username) {
		User user = userRepository.findByAccount(username)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		Like like = likeRepository.findByUserAndProduct(user, product)
			.orElseThrow(() -> new CustomException(ErrorCode.ALREADY_LIKED));

		likeRepository.delete(like);

		return LikeResponseDTO.registerLikeDTO.builder()
			.productId(productId)
			.userId(user.getId())
			.build();
	}
}
