package study.spring.hellobook.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;
import study.spring.hellobook.model.Books;
import study.spring.hellobook.service.BooksService;

@Service
@Slf4j
/** 도서 데이터 관리 기능을 제공하기 위한 Service 계층에 대한 구현체 */
// import study.spring.hellobook.service.BooksService;
public class BooksServiceImpl implements BooksService {

    /** MyBatis */
    // import org.apache.ibatis.session.SqlSession;
    @Autowired
    SqlSession sqlSession;

    /**
     * 도서 데이터 상세 조회
     * @param Books 조회할 도서의 일련번호를 담고 있는 Beans
     * @return 조회된 데이터가 저장된 Beans
     * @throws Exception
     */
    @Override
    public Books getBooksItem(Books input) throws Exception {
        Books result = null;

        try {
            result = sqlSession.selectOne("BooksMapper.selectItem", input);

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
     * 도서 데이터 목록 조회
     * @return 조회 결과에 대한 컬렉션
     * @throws Exception
     */
    @Override
    public List<Books> getBooksList(Books input) throws Exception {

        List<Books> result = null;

        try {
            result = sqlSession.selectList("BooksMapper.selectList", input);

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
     * 도서 데이터가 저장되어 있는 갯수 조회
     * @return int
     * @throws Exception
     */
    @Override
    public int getBooksCount(Books input) throws Exception {
        int result = 0;

        try {
            result = sqlSession.selectOne("BooksMapper.selectCountAll", input);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("데이터 조회에 실패했습니다.");
        }

        return result;
    }

    /**
     * 도서 데이터 등록하기
     * @param Books 저장할 정보를 담고 있는 Beans
     */
    @Override
    public int addBooks(Books input) throws Exception {
        int result = 0;

        try {
            result = sqlSession.insert("BooksMapper.insertItem", input);

            if (result == 0) {
                throw new NullPointerException("result=0");
            }
        }  catch (NullPointerException e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("저장된 데이터가 없습니다.");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("데이터 저장에 실패했습니다.");
        }

        return result;
    }
    

    /**
     * 도서 데이터 수정하기
     * @param Books 수정할 정보를 담고 있는 Beans
     * @throws Exception
     */
    @Override
    public int editBooks(Books input) throws Exception {
        int result = 0;

        try {
            result = sqlSession.update("BooksMapper.updateItem", input);

            if (result == 0) {
                throw new NullPointerException("result=0");
            }
        } catch (NullPointerException e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("수정된 데이터가 없습니다.");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("데이터 수정에 실패했습니다.");
        }

        return result;
    }

    /**
     * 도서 데이터 삭제하기
     * @param Books 삭제할 도서의 일련번호를 담고 있는 Beans
     * @throws Exception
     */
    @Override
    public int deleteBooks(Books input) throws Exception {
        int result = 0;

        try {
            // 도서 삭제 전 자신을 참조하는 학생들의 id컬럼을 null로 수정
        	//sqlSession.update("StudentMapper.unsetBooks", input);

            result = sqlSession.delete("BooksMapper.deleteItem", input);

            if (result == 0) {
                throw new NullPointerException("result=0");
            }
        } catch (NullPointerException e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("삭제된 데이터가 없습니다.");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new Exception("데이터 삭제에 실패했습니다.");
        }

        return result;
    }
}