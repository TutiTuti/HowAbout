package com.howabout.there.findcourse;

import java.sql.Timestamp;

public class restDto {
	private String r_name       ;
	private String r_phone      ;
	private String r_lon        ;
	private String r_lat        ;
	private String r_do         ;
	private String r_si         ;
	private String r_gu         ;
	private String r_dong       ;
	private String r_cat        ;
	private String r_id         ;
	private String r_url        ;
	private Timestamp r_insert_time;
	private Timestamp r_update_time;
	private String r_image_url ;
	public String getR_name() {
		return r_name;
	}
	public void setR_name(String r_name) {
		this.r_name = r_name;
	}
	public String getR_phone() {
		return r_phone;
	}
	public void setR_phone(String r_phone) {
		this.r_phone = r_phone;
	}
	public String getR_lon() {
		return r_lon;
	}
	public void setR_lon(String r_lon) {
		this.r_lon = r_lon;
	}
	public String getR_lat() {
		return r_lat;
	}
	public void setR_lat(String r_lat) {
		this.r_lat = r_lat;
	}
	public String getR_do() {
		return r_do;
	}
	public void setR_do(String r_do) {
		this.r_do = r_do;
	}
	public String getR_si() {
		return r_si;
	}
	public void setR_si(String r_si) {
		this.r_si = r_si;
	}
	public String getR_gu() {
		return r_gu;
	}
	public void setR_gu(String r_gu) {
		this.r_gu = r_gu;
	}
	public String getR_dong() {
		return r_dong;
	}
	public void setR_dong(String r_dong) {
		this.r_dong = r_dong;
	}
	public String getR_cat() {
		return r_cat;
	}
	public void setR_cat(String r_cat) {
		this.r_cat = r_cat;
	}
	public String getR_id() {
		return r_id;
	}
	public void setR_id(String r_id) {
		this.r_id = r_id;
	}
	public String getR_url() {
		return r_url;
	}
	public void setR_url(String r_url) {
		this.r_url = r_url;
	}
	public Timestamp getR_insert_time() {
		return r_insert_time;
	}
	public void setR_insert_time(Timestamp r_insert_time) {
		this.r_insert_time = r_insert_time;
	}
	public Timestamp getR_update_time() {
		return r_update_time;
	}
	public void setR_update_time(Timestamp r_update_time) {
		this.r_update_time = r_update_time;
	}
	public String getR_image_url() {
		return r_image_url;
	}
	public void setR_image_url(String r_image_url) {
		this.r_image_url = r_image_url;
	}
	@Override
	public String toString() {
		return "restDto [r_name=" + r_name + ", r_phone=" + r_phone + ", r_lon=" + r_lon + ", r_lat=" + r_lat
				+ ", r_do=" + r_do + ", r_si=" + r_si + ", r_gu=" + r_gu + ", r_dong=" + r_dong + ", r_cat=" + r_cat
				+ ", r_id=" + r_id + ", r_url=" + r_url + ", r_insert_time=" + r_insert_time + ", r_update_time="
				+ r_update_time + ", r_image_url=" + r_image_url + "]";
	}
	
	
	
	
}
