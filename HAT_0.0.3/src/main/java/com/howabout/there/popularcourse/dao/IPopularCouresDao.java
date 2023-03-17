package com.howabout.there.popularcourse.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.howabout.there.popularcourse.dto.PopularCourseDto;

@Mapper
public interface IPopularCouresDao {
	
	//카테고리별 리스트 반환
	public ArrayList<PopularCourseDto> setCourseCat(@Param("writer")String writer,@Param("gender")String gender, @Param("age") String age, @Param("do") String location_do, @Param("si") String location_si);
	
	//DB기준 Do 반환
	public ArrayList<String> setDoList();
	
	//Do기준 Si 반환
	public ArrayList<String> setSiList(@Param("location_do") String location_do);
	
	//로그인 상태 인기코스에서 찜되있는게 있는지 확인하는 Dao
	public int setFlag(@Param("writer")String writer,@Param("r_id")String r_id,@Param("c_id")String c_id );
}
