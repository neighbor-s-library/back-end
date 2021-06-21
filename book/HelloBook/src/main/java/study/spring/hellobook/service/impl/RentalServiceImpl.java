package study.spring.hellobook.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import study.spring.hellobook.model.Rental;
import study.spring.hellobook.service.RentalService;

/** 대여 데이터 관리 기능을 제공하기 위한 Service 계층에 대한 구현체 */
@Service
@Slf4j
public class RentalServiceImpl implements RentalService {

	/** MyBatis */
	// -> import org.apache.ibatis.session.SqlSession;
	@Autowired
	SqlSession sqlSession;

	/**
	 * 빌려준 책 데이터 수 조회
	 * @param Rental 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getOwnersBookCount(Rental input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("RentalMapper.selectCountBookOwner", input);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 조회에 실패했습니다.");
		}

		return result;
	}

	/**
	 * 빌려준 책 목록 조회
	 * @param Rental 빌려준 정보가 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	@Override
	public List<Rental> getOwnerList(Rental input) throws Exception {
		List<Rental> result = null;

        try {
            result = sqlSession.selectList("RentalMapper.selectItemOwner", input);

            if (result == null) {
                throw new NullPointerException("result=null");
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("데이터 조회에 실패했습니다.");
        }

        return result;
	}

	/**
	 * 빌린 책 데이터 수 조회
	 * @param Rental 검색조건을 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int getRentersBookCount(Rental input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("RentalMapper.selectCountBookRenter", input);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new Exception("데이터 조회에 실패했습니다.");
		}

		return result;
	}

	/**
	 * 빌린 책 목록 조회
	 * @param Rental 빌린 정보가 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	@Override
	public List<Rental> getRenterList(Rental input) throws Exception {
		List<Rental> result = null;

        try {
            result = sqlSession.selectList("RentalMapper.selectItemRenter", input);

            if (result == null) {
                throw new NullPointerException("result=null");
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("데이터 조회에 실패했습니다.");
        }

        return result;
	}

	/**
	 * Rental 테이블에 데이터 추가
	 * @param Rental 저장할 정보를 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int addItem(Rental input) throws Exception {
		int result = 0;

		try {
			result = sqlSession.insert("RentalMapper.insertItem", input);

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
	 * 반납상태 수정
	 * @param Rental 수정할 데이터를 담고 있는 Beans
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int updateState(Rental input) throws Exception {
		int result = 0;
		try {
			result= sqlSession.update("RentalMapper.updateRentalState", input);

			if(result == 0) {
				throw new NullPointerException("result == 0");
			}
			}catch(NullPointerException e) {
				log.error(e.getLocalizedMessage());
				throw new Exception("조회된 데이터가 없습니다.");
			}catch(Exception e) {
				log.error(e.getLocalizedMessage());
				throw new Exception("데이터 조회에 실패했습니다.");
			}
			return result;
	}

	/**
	 * Rental 데이터 상세 조회 (id로 조회)
	 * @param Rental 조회할 사용자의 일련번호를 담고 있는 Beans
	 * @return 조회된 데이터가 저장된 Beans
	 * @throws Exception
	 */
	@Override
	public Rental getRentalItem(Rental input) throws Exception {
		Rental result = null;

		try {
			result = sqlSession.selectOne("RentalMapper.selectBookState", input);

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

}
