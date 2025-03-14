package com.pricewagon.pricewagon.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByAccount(String account);

	boolean existsByAccount(String email);

}
