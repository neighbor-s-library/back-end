package study.spring.hellobook.model;

import lombok.Data;

/** 테이블 구조에 맞춘 Java Beans 생성 */
@Data
public class Books {
	private int id;
	private String title;
	private String writer;
	private String pub;
	private String detail;
	private String created_at;
	private String updated_at;
}
