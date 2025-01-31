package com.pricewagon.pricewagon.domain.alarm.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.history.service.ProductHistoryService;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.entity.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AlarmConverter {
	private final ProductHistoryService productHistoryService;

	public List<BasicProductInfo> convertToBasicProductInfo(List<Alarm> alarms) {
		return alarms.stream()
			.map(like -> {
				Product product = like.getProduct();
				Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);

				return new BasicProductInfo(
					product.getProductNumber(),
					product.getName(),
					product.getBrand(),
					product.getStarScore(),
					product.getReviewCount(),
					product.getUserLikeCount(),
					product.getImgUrl(),
					product.getShopType(),
					product.getCurrentPrice(),
					previousPrice,
					true
				);
			})
			.toList();
	}
}
