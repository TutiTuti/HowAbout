package com.howabout.there.findcourse;

import java.sql.Timestamp;

public class myCourseDto {
	private int id;
	private String writer;
	private String rest_name;
	private String rest_id;
	private String cafe_name;
	private String cafe_id;
	private Timestamp c_insert_time;
	private Timestamp c_update_time;
	private int	gender;
	private String birth;
	private int flag;
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getRest_name() {
		return rest_name;
	}
	public void setRest_name(String rest_name) {
		this.rest_name = rest_name;
	}
	public String getRest_id() {
		return rest_id;
	}
	public void setRest_id(String rest_id) {
		this.rest_id = rest_id;
	}
	public String getCafe_name() {
		return cafe_name;
	}
	public void setCafe_name(String cafe_name) {
		this.cafe_name = cafe_name;
	}
	public String getCafe_id() {
		return cafe_id;
	}
	public void setCafe_id(String cafe_id) {
		this.cafe_id = cafe_id;
	}
	public Timestamp getC_insert_time() {
		return c_insert_time;
	}
	public void setC_insert_time(Timestamp timestamp) {
		this.c_insert_time = timestamp;
	}
	public Timestamp getC_update_time() {
		return c_update_time;
	}
	public void setC_update_time(Timestamp timestamp) {
		this.c_update_time = timestamp;
	}
	@Override
	public String toString() {
		return "myCourseDto [id=" + id + ", writer=" + writer + ", rest_name=" + rest_name + ", rest_id=" + rest_id
				+ ", cafe_name=" + cafe_name + ", cafe_id=" + cafe_id + ", c_insert_time=" + c_insert_time
				+ ", c_update_time=" + c_update_time + ", gender=" + gender + ", birth=" + birth + "]";
	}
	
	
	
}
