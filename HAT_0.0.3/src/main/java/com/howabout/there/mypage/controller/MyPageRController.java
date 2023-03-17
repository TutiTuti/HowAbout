package com.howabout.there.mypage.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.howabout.there.mypage.dto.UserDto;
import com.howabout.there.mypage.service.MyPageService;
import com.howabout.there.sign.dto.SignUpDto;
import com.howabout.there.sign.vo.UserVo;
import com.howabout.there.token.JWTUtil;

@RestController
@RequestMapping("/myPage/myInfo")
public class MyPageRController {
	
	@Autowired
	MyPageService myPageService;
	
	@Autowired
	JWTUtil util;
	
	//유저 정보 업데이트. 회원정보 수정
	@PostMapping("/updateInfo")
	public Map updateUser(HttpServletRequest request, @RequestBody UserVo uservo){
		String tokenkey = request.getHeader("Authorization").substring(7);
		String userNick = util.getUserNickFromToken(tokenkey);
		String userId = util.getUserIdFromToken(tokenkey);
		uservo.setU_id(userId);
		Map user = myPageService.userUpdate(uservo, userNick);
		
		return user;
	}
	
	//회원탈퇴. 비밀번호 확인 -> 비밀번호가 일치하면 1 반환. 서버에서 flag 0으로 업데이트
	@PostMapping("/withdrawal")
	public int withdrawl(HttpServletRequest request,@RequestBody ArrayList<JSONObject> data) throws ParseException {
		String tokenkey = request.getHeader("Authorization").substring(7);
		UserDto userUp = myPageService.userListUp(util.getUserIdFromToken(tokenkey));
		BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
		if(! encoder.matches((String) data.get(0).get("u_pw") ,userUp.getU_pw() )) { 
			return 0;
		}else {
			JSONObject userId = new JSONObject();
			userId.put("u_id", util.getUserIdFromToken(tokenkey));
			data.add(userId);
			return myPageService.withdrawal(data);
		}
		

	}
	
	//비밀번호 확인. 비밀번호 일치하면 1, 불일치하면 0 반환.
	@PostMapping("/CheckPW")
	public int checkPW(HttpServletRequest request, @RequestBody Map data) throws ParseException {
		String tokenkey = request.getHeader("Authorization").substring(7);
		UserDto userUp = myPageService.userListUp(util.getUserIdFromToken(tokenkey));
		System.out.println("유저정보 잘 가지오 왔는가 ?? : "+ userUp.getU_id());
		BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
		System.out.println("111"+userUp.getU_pw());
		System.out.println("222"+data.get("u_pw").toString());
		if(! encoder.matches( (String) data.get("u_pw") ,userUp.getU_pw())) {
			System.out.println("비번 틀렸으");
			return 0;
		}else {
			System.out.println("비번 맞았으!");
			return 1;
		}

	}
	
	//유저정보 가지고 오기
	@PostMapping("/getMyData")
	public UserDto userUp(HttpServletRequest request){
		String header = request.getHeader("Authorization");
		String tokenkey = header.substring(7);
		//토큰으로 user의 ID를 가져온다
		String userId = util.getUserIdFromToken(tokenkey);
		// userID로 유저의 정보를 가지고 온다
		UserDto userUp = myPageService.userListUp(userId);
		System.out.println("유저 정보 : " + userUp.toString());
//		System.out.println("user ID : " + userUp.getU_id());
		return userUp;
	}
	
	//TEST용 
	@PostMapping("/testlogin/token")
	public String testTOKEN (HttpServletRequest request) {
		System.out.println("444444433332211");
		String header = request.getHeader("Authorization");
		JWTUtil util = new JWTUtil();
		String ss = util.getUserIdFromToken(header);
		String zz = util.getUserNickFromToken(header);
		String qq = util.getUserBirthFromToken(header);
		System.out.println(ss + " " + zz + " "+ qq);
		return ss;
	}
	
}
