package com.howabout.there.popularcourse.service;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howabout.there.popularcourse.SetCourse;
import com.howabout.there.popularcourse.dao.IPopularCouresDao;
import com.howabout.there.popularcourse.dto.PopularCourseDto;
import com.howabout.there.token.JWTUtil;

@Service
public class PopularCourseService implements IPopularCourseService{

	@Autowired
	IPopularCouresDao popularDao;
	@Autowired
	JWTUtil util;
	//인기 리스트 반환
	public ArrayList<PopularCourseDto> setCourseCat(ArrayList<JSONObject> setCourseInfo , String token){
		String writer="";
		SetCourse setInfo = new SetCourse();
		setInfo.setInfo(setCourseInfo);
		ArrayList<PopularCourseDto> courseReturn = popularDao.setCourseCat(writer,setInfo.getGender(), setInfo.getAge(), setInfo.getLocation_do(), setInfo.getLocation_si());
		
		if( token!="NO") {
			String userNick = util.getUserNickFromToken(token);	
			for(int i=0; i< courseReturn.size();i++) {
				int userCourseFlag = 0;
				try {
					userCourseFlag = popularDao.setFlag(userNick , courseReturn.get(i).getR_id(),courseReturn.get(i).getC_id());
				}catch(Exception e ) {
					
				}
				courseReturn.get(i).setFlag(userCourseFlag);
			}
		}else {
			for(int i=0; i< courseReturn.size();i++) {
				courseReturn.get(i).setFlag(0);
			}
		}
		return courseReturn;
	}
	
	public ArrayList<String> setDoList(){
		ArrayList<String> returnDoList = popularDao.setDoList();
		returnDoList.add(0, "전체");
		return returnDoList;
	}
	
	public ArrayList<String> setSiList(String choiceDo){
		String setSi; 
		System.out.println(choiceDo);
		if(choiceDo.equals("전체")) {
			setSi = "";
		}else {
			setSi = " r_do= "+choiceDo+" ";
		}
		ArrayList<String> returnDoList = popularDao.setSiList(setSi);
		returnDoList.add(0, "전체");
		return returnDoList;
	}
	
}
