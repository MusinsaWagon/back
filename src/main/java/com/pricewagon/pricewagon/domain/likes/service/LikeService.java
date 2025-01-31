package com.pricewagon.pricewagon.domain.likes.service;

import java.util.List;

import com.pricewagon.pricewagon.domain.likes.dto.LikeResponseDTO;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;

public interface LikeService {
	LikeResponseDTO.registerLikeDTO registerLike(Integer productNumber, String username);

	List<BasicProductInfo> getLikeList(String username);
}
