package com.pricewagon.pricewagon.domain.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class registerLikeDTO {
		Integer productId;
		Long userId;
		String action; // 좋아요 등록, 좋아요 삭제
	}
}
