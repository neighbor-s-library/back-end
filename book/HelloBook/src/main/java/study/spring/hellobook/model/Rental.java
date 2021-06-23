package study.spring.hellobook.model;

import lombok.Data;

/** 테이블 구조에 맞춘 Java Beans 생성 */
@Data
public class Rental {
	// 1) 기본 컬럼
	private int id;
	private int book_id;
	private int owner_id;
	private int renter_id;
	private String isrent;
	private String created_at;
	private String returndate;
	private String updated_at;
	
	// 2) JOIN절에 따른 추가 컬럼
	private String title;
	private String img;
	private String book_created_at;

}
