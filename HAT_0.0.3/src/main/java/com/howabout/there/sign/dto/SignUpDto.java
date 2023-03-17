package com.howabout.there.sign.dto;

import java.sql.Timestamp;

public class SignUpDto {

		private String u_nick;
		private String u_id;
		private String u_pw;
		private String u_insert_id;
		private Timestamp u_insert_time;
		private String u_update_id;
		private Timestamp u_update_time;
		private String birth;
		private int gender;
		private int u_flag;
		private String u_email;
		
		
		public String getU_nick() {
			return u_nick;
		}
		public void setU_nick(String u_nick) {
			this.u_nick = u_nick;
		}
		public String getU_id() {
			return u_id;
		}
		public void setU_id(String u_id) {
			this.u_id = u_id;
		}
		public String getU_pw() {
			return u_pw;
		}
		public void setU_pw(String u_pw) {
			this.u_pw = u_pw;
		}
		public String getU_insert_id() {
			return u_insert_id;
		}
		public void setU_insert_id(String u_insert_id) {
			this.u_insert_id = u_insert_id;
		}
		public Timestamp getU_insert_time() {
			return u_insert_time;
		}
		public void setU_insert_time(Timestamp u_insert_time) {
			this.u_insert_time = u_insert_time;
		}
		public String getU_update_id() {
			return u_update_id;
		}
		public void setU_update_id(String u_update_id) {
			this.u_update_id = u_update_id;
		}
		public Timestamp getU_update_time() {
			return u_update_time;
		}
		public void setU_update_time(Timestamp u_update_time) {
			this.u_update_time = u_update_time;
		}
		public String getBirth() {
			return birth;
		}
		public void setBirth(String birth) {
			this.birth = birth;
		}
		public int getGender() {
			return gender;
		}
		public void setGender(int gender) {
			this.gender = gender;
		}
		public int getU_flag() {
			return u_flag;
		}
		public void setU_flag(int u_flag) {
			this.u_flag = u_flag;
		}
		public String getU_email() {
			return u_email;
		}
		public void setU_email(String u_email) {
			this.u_email = u_email;
		}
		
		
	}
