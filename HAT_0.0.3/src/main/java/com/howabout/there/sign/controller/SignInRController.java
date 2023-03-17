package com.howabout.there.sign.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.howabout.there.sign.dao.ISignUpDao;
import com.howabout.there.sign.dto.LoginDto;
import com.howabout.there.sign.service.SignInService;
import com.howabout.there.sign.service.SignUpService;
import com.howabout.there.sign.service.mailSendService;
import com.howabout.there.test.mailTest;


@RestController
public class SignInRController {
	
	@Autowired
	SignInService signIn;
	
	
	
	@Autowired
	mailSendService mail;
	
	//로그인 기능 컨트롤러
	@PostMapping("/login/signIn")
	public LoginDto loginCheck(@RequestBody Map data) throws ParseException {
		System.out.println("@@##$$%%^^&&**(())!!@@##$$%%^^&&**((((");
		System.out.println("H_0.0.3"+data.get("u_id"));
		LoginDto userDto = new LoginDto();
		userDto = signIn.loginCheck(data);
		return userDto;
	}
	
	
	//아이디 찾기
	@PostMapping("/login/findMyId")
	public Map<String,String> findMyId(@RequestBody Map idHint) {
		System.out.println("abcd  "+idHint.toString());
		String myId = signIn.findMyId(idHint);
		Map returnId = new HashMap<>();
		returnId.put("u_id", myId);
		return returnId;
	}
	
	//비밀번호 재설정 내정보 확인
	@PostMapping("/login/checkMyInfo")
	public int beforeCheckMyInfo(@RequestBody Map pwmyInfo) {
		int myinfoCheck = signIn.infoCheck(pwmyInfo);
		return myinfoCheck;
	}
	//비밀번호 재설정
	@PostMapping("/login/setNewPw")
	public int setNewPw(@RequestBody Map myNewPw) {
		int newPwResult = signIn.setNewPw(myNewPw);
		return 1;
	}
	
	//이메일로 아이디를 보내서 아이디 찾기
	@PostMapping("/login/sendIdEmail")
	public int sendIdtoEmail(@RequestBody  Map userMail) {
		System.out.println("adasddd"+userMail.toString());
		System.out.println("보낸 이메일 값 @!@# : "+String.valueOf(userMail.get("u_email")));
		int sendId = signIn.sendId(userMail, 1);
		return sendId;
	}
	
	//NEW 비밀번호 재설정 > 이메일로 인증번호 보내기
	@PostMapping("/login/sendPwEmail")
	public int sendPwEmail(@RequestBody Map userMail) {
		signIn.sendAuth(userMail);
		return 1;
	}
	
}