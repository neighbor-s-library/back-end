package study.spring.hellobook.service;

import java.util.List;

import study.spring.hellobook.model.Rental;

/** 대여 데이터 관리 기능을 제공하기 위한 Service 계층 */
public interface RentalService {

	/**
	 * 빌려준 책 데이터 수 조회
	 * @param Rental 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int getOwnersBookCount(Rental input) throws Exception;
	
	/**
	 * 빌려준 책 목록 조회
	 * @param Rental 빌려준 정보가 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	public List<Rental> getOwnerList(Rental input) throws Exception;
	
	/**
	 * 빌린 책 데이터 수 조회
	 * @param Rental 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int getRentersBookCount(Rental input) throws Exception;
	
	/**
	 * 빌린 책 목록 조회
	 * @param Rental 빌린 정보가 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	public List<Rental> getRenterList(Rental input) throws Exception;
	
	/**
	 * Rental 테이블에 데이터 추가
	 * @param Rental 저장할 정보를 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int addItem(Rental input) throws Exception;
	
	/**
	 * 반납일정 수정
	 * @param Rental 수정할 데이터를 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	public int updateItem(Rental input) throws Exception;
}
