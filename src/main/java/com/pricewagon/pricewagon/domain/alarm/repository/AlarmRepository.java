package com.pricewagon.pricewagon.domain.alarm.repository;

import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

}
