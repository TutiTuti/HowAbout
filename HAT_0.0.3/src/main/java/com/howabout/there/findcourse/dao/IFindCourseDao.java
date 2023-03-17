package com.howabout.there.findcourse.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.howabout.there.findcourse.cafeDto;
import com.howabout.there.findcourse.myCourseDto;
import com.howabout.there.findcourse.restDto;


@Mapper
public interface IFindCourseDao {

	//리뷰가 있으면 가지고 오기
	public Map getReview(@Param("url")String url);
	// 크롤링 리뷰 저장하기
	public void inputCrawler(@Param("url")String url,@Param("imgUrl")String imgUrl,
			 @Param("storeTime")String storeTime, @Param("star")String star,@Param("review1")String review1,
			 @Param("review2")String review2, @Param("review3")String review3,@Param("insertTime")Timestamp time);	
	
}
