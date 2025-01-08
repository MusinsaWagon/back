package com.pricewagon.pricewagon.domain.alarm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.alarm.entity.AlarmStatus;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	Optional<Alarm> findById(Long alarmId);

	List<Alarm> findByStatus(AlarmStatus status);

	@Query("SELECT a FROM Alarm a " +
		"JOIN FETCH a.product p " +
		"JOIN FETCH a.user u " +
		"LEFT JOIN FETCH u.fcmTokens " +
		"WHERE a.status = :status")
	List<Alarm> findActiveAlarmsWithDetails(@Param("status") AlarmStatus status);

	@Modifying
	@Query("UPDATE Alarm a SET a.status = :status WHERE a.id = :id AND a.status = :currentStatus")
	int updateAlarmStatus(@Param("id") Long id, @Param("status") AlarmStatus status,
		@Param("currentStatus") AlarmStatus currentStatus);

}


