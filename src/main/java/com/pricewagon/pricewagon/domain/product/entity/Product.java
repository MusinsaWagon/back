package com.pricewagon.pricewagon.domain.product.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.common.BaseAuditEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("상품 이미지 URL")
	@Column(length = 200)
	private String imgUrl;

	@Comment("상품이름")
	@Column(length = 100, nullable = false)
	private String name;

	@Comment("상품 번호")
	@Column(name = "product_num", nullable = false, unique = true)
	private Integer productNumber;

	@Comment("브랜드 이름")
	@Column(length = 100, nullable = false)
	private String brand;

	@Comment("원가")
	@Column(nullable = false)
	private Integer originPrice;

	@Comment("별점")
	@Column(nullable = false)
	private Double starScore;

	@Comment("리뷰 수")
	@Column(nullable = false)
	private Integer reviewCount;

	@Comment("좋아요 수")
	@Column(nullable = false)
	private Integer likeCount;

	@Comment("쇼핑몰 타입")
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private ShopType shopType;

	@Comment("카테고리")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private ProductDetail productDetail;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductHistory> productHistories = new ArrayList<>();
}