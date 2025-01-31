package com.pricewagon.pricewagon.domain.likes.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.likes.dto.LikeResponseDTO;
import com.pricewagon.pricewagon.domain.likes.service.LikeService;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
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

	@Operation(summary = "좋아요 등록 및 삭제", description = "좋아요 등록 및 삭제하는 기능(토큰 필요)")
	@PostMapping("/{productNumber}")
	public ResponseEntity<LikeResponseDTO.registerLikeDTO> registerLike(
		@PathVariable Integer productNumber,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		LikeResponseDTO.registerLikeDTO result = likeService.registerLike(productNumber, userDetails.getUsername());
		return ResponseEntity.ok(result);
	}

	@Operation(summary = "좋아요 목록 조회", description = "좋아요 목록 조회하는 기능(토큰 필요)")
	@GetMapping("/list")
	public List<BasicProductInfo> getLikeList(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return likeService.getLikeList(userDetails.getUsername());

	}

}
