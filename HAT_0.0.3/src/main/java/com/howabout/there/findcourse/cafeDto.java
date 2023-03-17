package com.howabout.there.findcourse;

import java.sql.Timestamp;

public class cafeDto {
	private String c_name       ;
	private String c_phone      ;
	private String c_lon        ;
	private String c_lat        ;
	private String c_do         ;
	private String c_si         ;
	private String c_gu         ;
	private String c_dong       ;
	private String c_cat        ;
	private String c_id         ;
	private String c_url        ;
	private Timestamp c_insert_time;
	private Timestamp c_update_time;
	private String c_image_url;
	
	public String getC_image_url() {
		return c_image_url;
	}
	public void setC_image_url(String c_image_url) {
		this.c_image_url = c_image_url;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public String getC_phone() {
		return c_phone;
	}
	public void setC_phone(String c_phone) {
		this.c_phone = c_phone;
	}
	public String getC_lon() {
		return c_lon;
	}
	public void setC_lon(String c_lon) {
		this.c_lon = c_lon;
	}
	public String getC_lat() {
		return c_lat;
	}
	public void setC_lat(String c_lat) {
		this.c_lat = c_lat;
	}
	public String getC_do() {
		return c_do;
	}
	public void setC_do(String c_do) {
		this.c_do = c_do;
	}
	public String getC_si() {
		return c_si;
	}
	public void setC_si(String c_si) {
		this.c_si = c_si;
	}
	public String getC_gu() {
		return c_gu;
	}
	public void setC_gu(String c_gu) {
		this.c_gu = c_gu;
	}
	public String getC_dong() {
		return c_dong;
	}
	public void setC_dong(String c_dong) {
		this.c_dong = c_dong;
	}
	public String getC_cat() {
		return c_cat;
	}
	public void setC_cat(String c_cat) {
		this.c_cat = c_cat;
	}
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String c_id) {
		this.c_id = c_id;
	}
	public String getC_url() {
		return c_url;
	}
	public void setC_url(String c_url) {
		this.c_url = c_url;
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
		return "cafeDto [c_name=" + c_name + ", c_phone=" + c_phone + ", c_lon=" + c_lon + ", c_lat=" + c_lat
				+ ", c_do=" + c_do + ", c_si=" + c_si + ", c_gu=" + c_gu + ", c_dong=" + c_dong + ", c_cat=" + c_cat
				+ ", c_id=" + c_id + ", c_url=" + c_url + ", c_insert_time=" + c_insert_time + ", c_update_time="
				+ c_update_time + "]";
	}
	
	
	
}
