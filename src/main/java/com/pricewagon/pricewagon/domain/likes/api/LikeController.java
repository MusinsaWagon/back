package com.pricewagon.pricewagon.domain.likes.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.likes.dto.LikeResponseDTO;
import com.pricewagon.pricewagon.domain.likes.service.LikeService;
import com.pricewagon.pricewagon.global.config.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. [좋아요]", description = "좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController {
	private final LikeService likeService;

	@Operation(summary = "좋아요 등록", description = "좋아요 등록하는 기능")
	@PostMapping("/{productId}")
	public ResponseEntity<LikeResponseDTO.registerLikeDTO> registerLike(
		@PathVariable Long productId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikeResponseDTO.registerLikeDTO result = likeService.registerLike(productId, userDetails.getUsername());
		return ResponseEntity.ok(result);
	}

	@Operation(summary = "좋아요 취소", description = "좋아요 취소하는 기능")
	@PostMapping("/cancel/{productId}")
	public ResponseEntity<LikeResponseDTO.registerLikeDTO> cancelLike(
		@PathVariable Long productId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikeResponseDTO.registerLikeDTO result = likeService.cancelLike(productId, userDetails.getUsername());
		return ResponseEntity.ok(result);
	}

}
