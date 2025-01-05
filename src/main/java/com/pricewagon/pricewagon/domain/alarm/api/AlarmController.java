package com.pricewagon.pricewagon.domain.alarm.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.alarm.dto.AlarmRequestDTO;
import com.pricewagon.pricewagon.domain.alarm.dto.AlarmResponseDTO;
import com.pricewagon.pricewagon.domain.alarm.service.AlarmService.AlarmService;
import com.pricewagon.pricewagon.global.config.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "4. [알람]", description = "알람 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
	private final AlarmService alarmService;

	@Operation(summary = "알람 등록", description = "알람 등록하는 기능 (토큰 필요)")
	@PostMapping("/register")
	public ResponseEntity<AlarmResponseDTO.registerAlarmDTO> registerAlarm(
		@RequestBody AlarmRequestDTO.registerAlarm request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		AlarmResponseDTO.registerAlarmDTO result = alarmService.registerAlarm(request, userDetails.getUsername());
		return ResponseEntity.ok(result);
	}

}
