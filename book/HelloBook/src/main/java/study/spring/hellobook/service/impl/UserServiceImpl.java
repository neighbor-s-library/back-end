package study.spring.hellobook.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import study.spring.hellobook.model.Users;
import study.spring.hellobook.service.UserService;

/** 교수 데이터 관리 기능을 제공하기 위한 Service 계층에 대한 구현체 */
// -> import org.springframework.stereotype.Service;
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	/** MyBatis */
	// -> import org.apache.ibatis.session.SqlSession;
	@Autowired
	SqlSession sqlSession;

	@Override
	public int getUserCount(Users input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("userMapper.selectCountAll", input);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public Users getUserItem(Users input) throws Exception {
		Users result = null;

		try {
			result = sqlSession.selectOne("userMapper.selectItem", input);

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
	
	/** 로그인 아이디 비밀번호 검사 */
	@Override
	public Users checkIdPw(Users input) throws Exception {
		//결과를 리턴할 객체 생성
		Users result = null;

		try {
			result = sqlSession.selectOne("userMapper.idPwCheck", input);
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
	
	/** 회원가입 정보 저장 */
	@Override
	public int addUser(Users input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.insert("userMapper.insertUser", input);

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
	
	/** 아이디 중복 검사 */
	@Override
	public int getIdItem(Users input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("userMapper.idCheck", input);

			if (result > 0) {
				result = 1;
			}
		} catch (NullPointerException e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("조회된 데이터가 없습니다.");
		}

		return result;
	}


	

}
