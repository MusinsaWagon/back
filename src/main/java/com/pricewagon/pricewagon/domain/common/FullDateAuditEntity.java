package com.pricewagon.pricewagon.domain.common;

import java.time.LocalDate;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class FullDateAuditEntity extends CreatedDateEntity {
	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private LocalDate updatedAt;
}
