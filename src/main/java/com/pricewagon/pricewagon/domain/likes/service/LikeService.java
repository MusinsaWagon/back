package com.pricewagon.pricewagon.domain.likes.service;

import com.pricewagon.pricewagon.domain.likes.dto.LikeResponseDTO;

public interface LikeService {
	LikeResponseDTO.registerLikeDTO registerLike(Long productId, String username);

	LikeResponseDTO.registerLikeDTO cancelLike(Long productId, String username);
}
