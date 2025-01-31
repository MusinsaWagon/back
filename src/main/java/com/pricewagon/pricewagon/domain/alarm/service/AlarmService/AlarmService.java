package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import java.util.List;

import com.pricewagon.pricewagon.domain.alarm.dto.AlarmRequestDTO;
import com.pricewagon.pricewagon.domain.alarm.dto.AlarmResponseDTO;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;

public interface AlarmService {

	void checkAllAlarmsAndNotify();

	AlarmResponseDTO.registerAlarmDTO registerAlarm(AlarmRequestDTO.registerAlarm request, String email);

	List<BasicProductInfo> getAlarmList(String username);
}
