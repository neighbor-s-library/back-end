package study.spring.hellobook.model;

import lombok.Data;

/** 테이블 구조에 맞춘 Java Beans 생성 */
@Data
public class Users {
	
	private int id;
	private String email;
	private int point;
	private String address;
	private String nickname;
	private String created_at;
	private String updated_at;
	
	//Auth
	private String pw;
	private String tel;
	
}
