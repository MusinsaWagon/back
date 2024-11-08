package com.pricewagon.pricewagon.domain.product.entity;

import org.hibernate.annotations.Comment;

import com.pricewagon.pricewagon.domain.common.CreatedTimeEntity;
import com.pricewagon.pricewagon.domain.product.dto.request.ProductUrlRequest;
import com.pricewagon.pricewagon.domain.product.entity.type.ShopType;
import com.pricewagon.pricewagon.domain.product.entity.type.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRegistration extends CreatedTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Comment("쇼핑몰 타입")
	@Enumerated(EnumType.STRING)
	@Column(length = 50, nullable = false)
	private ShopType shopType;

	@Comment("등록 상품 URL")
	@Column(nullable = false, length = 200)
	private String url;

	@Comment("처리 상태")
	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = false)
	private Status status;

	@Builder(access = AccessLevel.PRIVATE)
	private ProductRegistration(
		ShopType shopType,
		String url
	) {
		this.shopType = shopType;
		this.url = url;
		this.status = Status.PENDING;
	}

	public static ProductRegistration create(
		ProductUrlRequest request
	) {
		return ProductRegistration.builder()
			.shopType(request.shopType())
			.url(request.url())
			.build();
	}
}
