package com.pricewagon.pricewagon.domain.common;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class CreatedDateEntity {
	@CreatedDate
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDate createdAt;
}
