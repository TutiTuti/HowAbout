package com.howabout.there.sign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.howabout.there.sign.dao.ISignInDao;

@Service
public class mailSendService {

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	ISignInDao signIn;
	
	SimpleMailMessage simple = new SimpleMailMessage();
	
	public void sendContent(String mail, String content, int function) {
		String setContent=null;
		String subject=null;
		switch(function) {
		case 1:
			setContent = "회원님의 아이디는 : "+content+" 입니다.";
			subject = "HowAboutThere 아이디 찾기";
			break;
		}
				
		simple.setTo(mail);
		simple.setSubject(subject);
		simple.setText(setContent);
		mailSender.send(simple);
	}
	
	public void sendMailAuth(String mailAdress) {
	
		simple.setTo(mailAdress);
		simple.setSubject("HowAboutThere 이메일 인증 코드 입니다.");
		String randomCode = "";

		for(int i =0; i<7;i++) {
			randomCode += (char)((int)(Math.random()*26)+65);
		}
		String resultValeu ="인증코드 : "+randomCode;
		signIn.setNewMailAuth(randomCode,"codeName"+randomCode);
		simple.setText(resultValeu);
		
		mailSender.send(simple);
	}
	
}
