package com.pricewagon.pricewagon.domain.alarm.api;

import com.pricewagon.pricewagon.domain.alarm.service.AlarmService.AlarmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "4. [알람]", description = "알람 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;

}
