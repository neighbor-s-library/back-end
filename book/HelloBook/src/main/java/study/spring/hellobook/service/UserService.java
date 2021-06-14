package study.spring.hellobook.service;


import study.spring.hellobook.model.Users;

/** 사용자 데이터 관리 기능을 제공하기 위한 Service 계층 */
public interface UserService {
	
	/**
	 * 교수 데이터가 저장되어 있는 갯수 조회
	 * @param Professor 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int getUserCount(Users input) throws Exception;
	
	/**
	 * 교수 데이터 상세 조회
	 * @param Professor 조회할 교수의 일련번호를 담고 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	public Users getUserItem(Users input) throws Exception;
	
	/** 로그인 아이디 비밀번호 검사 */
	public Users checkIdPw(Users input) throws Exception;
	
	/** 아이디 중복 검사*/
	public int getIdItem(Users input) throws Exception;
	
	/** 회원가입 정보 저장 */
	public int addUser(Users input) throws Exception;
	
}
