package com.pricewagon.pricewagon.domain.user.repository;

import com.pricewagon.pricewagon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
