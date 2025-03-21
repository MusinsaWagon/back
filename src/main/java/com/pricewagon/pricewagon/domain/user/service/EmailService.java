package com.pricewagon.pricewagon.domain.user.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.utils.RedisUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	private final RedisUtil redisUtil;
	private int authNumber;
	@Value("${spring.mail.username}")
	private String setFrom;

	//임의의 6자리 양수를 반환합니다.
	public void makeRandomNumber() {
		Random r = new Random();
		String randomNumber = "";
		for (int i = 0; i < 6; i++) {
			randomNumber += Integer.toString(r.nextInt(10));
		}

		authNumber = Integer.parseInt(randomNumber);
	}

	public String joinEmail(String email) {
		makeRandomNumber();
		String toMail = email;
		String title = "회원 가입 인증 이메일 입니다.";
		String content =
			"Cost Flower 인증 번호 입니다." +
				"<br><br>" +
				"인증 번호는 " + authNumber + "입니다." +
				"<br>" +
				"인증번호를 입력해주세요.";
		mailSend(setFrom, toMail, title, content);
		return Integer.toString(authNumber);
	}

	public void mailSend(String setFrom, String toMail, String title, String content) {
		MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
			// true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
			mailSender.send(message);
		} catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
			// 이러한 경우 MessagingException이 발생
			e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
		}
		redisUtil.setDataExpire(Integer.toString(authNumber), toMail, 60 * 5L);

	}

	public boolean validCheck(UserRequestDTO.validNumDTO request) {
		if (redisUtil.getData(request.getValidCode()) == null) {
			return false;
		} else if (redisUtil.getData(request.getValidCode()).equals(request.getEmail())) {
			return true;
		} else {
			return false;
		}
	}
}
