package com.pricewagon.pricewagon.domain.alarm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.alarm.entity.AlarmStatus;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	Optional<Alarm> findById(Long alarmId);

	List<Alarm> findByStatus(AlarmStatus status);
}


