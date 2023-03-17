package com.howabout.there.sign.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.howabout.there.sign.dto.SignUpDto;
import com.howabout.there.sign.vo.UserVo;

@Mapper
public interface ISignUpDao {
	
	//회원가입 user DB 등록 DAO
	public int signUp(SignUpDto signdto);
	//로그인 
	public int signIn(UserVo userDto);
	//회원가입 ID중복체크 DAO
	public boolean idCheck(String string);
	//회원가입 NICK중복체크 DAO
	public boolean nickCheck(String string);
	//회원가입 EMAIL중복체크 DAO
	public int emailCheck(String email);
	//회원가입 authTable email 추가하기
	public void authUp(@Param("u_email")String email);
	//이메일 인증 값 확인
	public int authCheck(@Param("email")String email,@Param("auth")String auth);
}
