package study.spring.hellobook.service;


import study.spring.hellobook.model.Auth;
import study.spring.hellobook.model.Users;

/** 사용자 데이터 관리 기능을 제공하기 위한 Service 계층 */
public interface UsersService {
	
	/**
	 * 전체 사용자 수 조회
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int getUserCount(Users input) throws Exception;
	
	/**
	 * 사용자 데이터 상세 조회 (id로 조회)
	 * @param Users 조회할 사용자의 일련번호를 담고 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	public Users getUserItem(Users input) throws Exception;
	
	/**
	 * 사용자 데이터 상세 조회 (email로 조회)
	 * @param Users 조회할 사용자의 일련번호를 담고 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	public Users getUserItem2(Users input) throws Exception;
	
	/**
	 * 로그인 아이디 비밀번호 검사
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	public Users checkIdPw(Users input) throws Exception;
	
	/**
	 * 아이디 중복 검사
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int getIdItem(Users input) throws Exception;
	
	/**
	 * 회원가입 정보 저장
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int addUser(Users input) throws Exception;
	
	/**
	 * 회원가입 정보 저장 - 개인정보
	 * @param Auth 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int addUser2(Auth input) throws Exception;
	
	/**
	 * 사용자 개인정보 조회
	 * @param Auth 검색조건을 담고 있는 Beans
	 * @return Auth
	 * @throws Exception
	 */
	public Auth getUsersInfo(Auth input) throws Exception;
	
	/**
	 * 사용자 정보 수정
	 * @param Users 수정할 데이터를 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int usersRevise(Users input) throws Exception;
	
	/**
	 * 사용자 정보 수정 (연락처, 비밀번호)
	 * @param Users 수정할 데이터를 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int usersInfoRevise(Auth input) throws Exception;
	
}
