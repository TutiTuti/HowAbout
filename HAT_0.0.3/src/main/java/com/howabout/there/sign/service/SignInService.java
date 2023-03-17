package com.howabout.there.sign.service;



import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.howabout.there.sign.dao.ISignInDao;
import com.howabout.there.sign.dao.ISignUpDao;
import com.howabout.there.sign.dto.LoginDto;
import com.howabout.there.sign.vo.UserVo;
import com.howabout.there.token.JWTUtil;


@Service
public class SignInService implements UserDetailsService  {

	@Autowired
	ISignInDao signDao;
	
	@Autowired
	ISignUpDao signupdao;
	
	@Autowired
	mailSendService mail;
	
	@Autowired
	private JWTUtil jwtutil;
	
	BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
	//로그인 체크
	public LoginDto loginCheck(Map signInData) throws ParseException {
		String returnValue = null;
		

		UserVo loginCheck;// = new UserVo();
		
		LoginDto resultDto = new LoginDto();
		
		
		loginCheck = signDao.userData(signInData.get("u_id").toString());
		
		//값이 없으면 47번째 줄에서 오류가 나옵니다.
		if(loginCheck == null) {
			System.out.println("LOGIN : ID ERROR");
			resultDto.setMsg("존재하지 않는 회원정보 입니다");
			resultDto.setSuccess(0);
		}
		else if (! encoder.matches(signInData.get("u_pw").toString(), loginCheck.getU_pw())) {
			System.out.println("LOGIN :PW ERROR");
			resultDto.setMsg("비밀번호가 일치하지 않습니다.");
			resultDto.setSuccess(0);
		}else {
			returnValue = jwtutil.generateToken(loginCheck, loginCheck.getU_id());
			System.out.println("login Token : " + returnValue);
			resultDto.setToken(returnValue);
			resultDto.setMsg(loginCheck.getU_nick());
			resultDto.setSuccess(1);
		}
		System.out.println("TOKEN : " + returnValue);
		return resultDto;
	}
	
	

	
	public String findMyId(Map idHint){
		String getMyId = signDao.findMyId(idHint.get("u_email").toString());
		return getMyId;
	}
	
	public int infoCheck(Map pwmyInfo){
		int checkMyInfo = signDao.checkMyInfo((String)pwmyInfo.get("u_email"), (String)pwmyInfo.get("u_id") );
		return checkMyInfo;
	}
	public int setNewPw(Map myNewPw) {
		int setMyPw = signDao.setMyPw((String)myNewPw.get("u_id" ),encoder.encode((String)myNewPw.get("u_pw")));
		return 1;
	}

	
	//유저의 id 값을 받아서 UserVo를 가져온 다음 UserDetails에 맞춰서 값을 넣어준다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("여기는 왔는가 ?" + username);
		UserVo userVo = new UserVo();
		System.out.println("name CHECK   "+username);
		userVo = signDao.userData(username);
		System.out.println("get USER VO ? : " + userVo.getU_id() + " " + userVo.getU_pw());
	
		return 	new org.springframework.security.core.userdetails.User(userVo.getU_id(),userVo.getU_pw(), new ArrayList<>());
	}
	
	
	//email값 받아서 유저 id를 메일로 보내기
	public int sendId(Map userData, int function) {
		String userId = signDao.getUserId(String.valueOf(userData.get("u_email")));
		if(userId == null) {
			return 0;
		}else {
		mail.sendContent(String.valueOf(userData.get("u_email")), userId,function);
		return 1;}
	}
	
	//email 받아서 인증번호 보내기 
	public int sendAuth(Map userEmail) {
		String email = userEmail.get("u_email").toString();
		int emailexist = signupdao.emailCheck(email);
		if(signupdao.emailCheck(email)==1) {
			mail.sendMailAuth(email);
			return 1;
		}else {
			return 0;
		}
		
		
		
		
	}
	
	
	
}
