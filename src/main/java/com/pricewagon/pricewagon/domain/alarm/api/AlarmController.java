package com.pricewagon.pricewagon.domain.alarm.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.alarm.service.AlarmService.AlarmService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "4. [알람]", description = "알람 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
	private final AlarmService alarmService;

	// @GetMapping(value="/subscribe/{userId}", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
	// public SseEmitter subscribe(@PathVariable(name="userId") Long userId, @RequestHeader(value="Last-Event-ID", required = false, defaultValue="") String lastEventId){
	// 	// 나중에 토큰에서 가져오도록 수정
	// 	return alarmService.subscribe(userId,lastEventId);
	// }
	//
	// @PostMapping("/subscribe")
	// public void send(@PathVariable Long userId, @RequestBody){
	//
	// }

}
