package study.spring.hellobook.model;

import lombok.Data;

/** 테이블 구조에 맞춘 Java Beans 생성 */
@Data
public class Auth {
	private int user_id;
	private String pw;
	private int tel;
}
