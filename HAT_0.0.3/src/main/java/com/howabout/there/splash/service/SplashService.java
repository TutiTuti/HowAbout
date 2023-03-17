package com.howabout.there.splash.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howabout.there.sign.dao.ISignInDao;
import com.howabout.there.sign.vo.UserVo;
import com.howabout.there.token.JWTUtil;

@Service
public class SplashService {
	
	@Autowired
	JWTUtil util;
	@Autowired
	ISignInDao dao;
	
	public Map getToken(String token) {
		Map returnMap = new HashMap<>();
		UserVo user = dao.userData(util.getUserIdFromToken(token));
		String newToken = util.generateToken(user, user.getU_id());
		returnMap.put("jwt", newToken);
		return returnMap;
	}
	

}
