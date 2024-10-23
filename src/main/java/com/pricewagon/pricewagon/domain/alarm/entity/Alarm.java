package com.pricewagon.pricewagon.domain.alarm.entity;

import com.pricewagon.pricewagon.domain.common.BaseEntity;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class Alarm extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable= false)
	private Integer desired_price;

	@Column(nullable= false)
	private LocalDate start_date;

	@Column(nullable= false)
	private LocalDate end_date;

	@Enumerated(EnumType.STRING)
	@Column(nullable= false)
	private AlarmStatus status;

}
