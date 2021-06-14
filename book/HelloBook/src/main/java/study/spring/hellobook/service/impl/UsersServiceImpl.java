package study.spring.hellobook.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import study.spring.hellobook.model.Auth;
import study.spring.hellobook.model.Users;
import study.spring.hellobook.service.UsersService;

/** 사용자 데이터 관리 기능을 제공하기 위한 Service 계층에 대한 구현체 */
// -> import org.springframework.stereotype.Service;
@Service
@Slf4j
public class UsersServiceImpl implements UsersService {

	/** MyBatis */
	// -> import org.apache.ibatis.session.SqlSession;
	@Autowired
	SqlSession sqlSession;

	/**
	 * 전체 사용자 수 조회
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getUserCount(Users input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("UsersMapper.selectCountAll", input);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 조회에 실패했습니다.");
		}

		return result;
	}

	/**
	 * 사용자 데이터 상세 조회
	 * @param Users 조회할 사용자의 일련번호를 담고 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	@Override
	public Users getUserItem(Users input) throws Exception {
		Users result = null;

		try {
			result = sqlSession.selectOne("UsersMapper.selectItem", input);

			if (result == null) {
				throw new NullPointerException("result=null");
			}
		} catch (NullPointerException e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("조회된 데이터가 없습니다.");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 조회에 실패했습니다.");
		}
		return result;
	}
	
	/**
	 * 로그인 아이디 비밀번호 검사
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	@Override
	public Users checkIdPw(Users input) throws Exception {
		//결과를 리턴할 객체 생성
		Users result = null;

		try {
			result = sqlSession.selectOne("UsersMapper.idPwCheck", input);
			if (result == null) {
				throw new NullPointerException("result == null");
			}
		} catch (NullPointerException e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("비밀번호가 잘못 되었습니다.");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 조회에 실패했습니다.");
		}
		return result;
	}
	
	/**
	 * 회원가입 정보 저장
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int addUser(Users input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.insert("UsersMapper.insertUser", input);

			if (result == 0) {
				throw new NullPointerException("result=0");
			}
		} catch (NullPointerException e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("저장된 데이터가 없습니다.");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 저장에 실패했습니다.");
		}
		return result;
	}
	
	/**
	 * 아이디 중복 검사
	 * @param Users 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getIdItem(Users input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("UsersMapper.idCheck", input);

			if (result > 0) {
				result = 1;
			}
		} catch (NullPointerException e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("조회된 데이터가 없습니다.");
		}

		return result;
	}

	/**
	 * 회원가입 정보 저장 - 개인정보
	 * @param Auth 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int addUser2(Auth input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.insert("UsersMapper.insertUser2", input);

			if (result == 0) {
				throw new NullPointerException("result=0");
			}
		} catch (NullPointerException e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("저장된 데이터가 없습니다.");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 저장에 실패했습니다.");
		}
		return result;
	}


	

}
