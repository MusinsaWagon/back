package com.pricewagon.pricewagon.domain.alarm.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.pricewagon.pricewagon.domain.alarm.dto.AlarmRequestDTO;
import com.pricewagon.pricewagon.domain.alarm.service.AlarmService.AlarmService;
import com.pricewagon.pricewagon.global.config.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "4. [알람]", description = "알람 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
	private final AlarmService alarmService;

	@Operation(summary = "알람 연결", description = "알람 연결")
	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> subscribe(
		@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return ResponseEntity.ok(alarmService.subscribe(userDetails.getUsername(), lastEventId));
	}

	@Operation(summary = "알람 등록", description = "알람 등록하는 기능")
	@PostMapping("/register")
	public ResponseEntity<Void> registerAlarm(@RequestBody AlarmRequestDTO.registerAlarm request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		alarmService.registerAlarm(request, userDetails.getUsername());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "알람 전송", description = "알람 전송하는 기능")
	@PostMapping("/send")
	public ResponseEntity<String> sendAlarm(@RequestBody AlarmRequestDTO.FCMAlarmRequestDTO request) {
		String response = alarmService.sendAlarmByToken(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "알림 보내기", description = "알림 보내기")
	@PostMapping("/send")
	public ResponseEntity<Void> sendAlarm(@RequestBody AlarmRequestDTO.sendAlarm request) {
		alarmService.sendAlarm(request);
		return ResponseEntity.ok().build();
	}
}
