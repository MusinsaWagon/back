package com.pricewagon.pricewagon.domain.alarm.service.AlarmService;

import com.pricewagon.pricewagon.domain.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService{
    private final AlarmRepository alarmRepository;

}
