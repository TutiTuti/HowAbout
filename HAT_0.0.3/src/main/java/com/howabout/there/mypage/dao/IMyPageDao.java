package com.howabout.there.mypage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.howabout.there.mypage.dto.UserDto;
import com.howabout.there.sign.vo.UserVo;

@Mapper
public interface IMyPageDao {
	
	//회원정보 수정. id값으로 수정 내용 update
	public int updateUser(UserVo uservo);
	
	//회원정보 select
	public UserDto selectUser(String id);
	
	//비밀번호 확인. 비밀번호 일치하면 1, 불일치하면 0
	public int pwCheck(String u_id, String u_pw);
	
	// 회원탈퇴 flag를 0으로 바꿈
	public void withdrawal(String id);
	//회원탈퇴 사유를 저장
	public void reason(String why);

	//정보변경 course Nick먼저 바꾸기
	public void changeWriter(@Param("writer")String u_nick, @Param("u_nick")String nick);
}
