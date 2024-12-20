package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import com.pricewagon.pricewagon.domain.alarm.dto.AlarmRequestDTO;
import com.pricewagon.pricewagon.domain.alarm.dto.AlarmResponseDTO;

public interface AlarmService {

	void checkAllAlarmsAndNotify();

	AlarmResponseDTO.registerAlarmDTO registerAlarm(AlarmRequestDTO.registerAlarm request, String email);

}
