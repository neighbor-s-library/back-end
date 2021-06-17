package study.spring.hellobook.service;

import java.util.List;

import study.spring.hellobook.model.Books;
import study.spring.hellobook.model.Users_books;

public interface BooksService {
    /**
     * 도서 데이터 상세 조회
     * @param Books 조회할 도서의 일련번호를 담고 있는 Beans
     * @return 조회할 데이터가 저장된 Beans
     * @throws Eception
     */
    public Books getBooksItem(Books input) throws Exception;

    /**
     * 도서 데이터 목록 조회
     * @param Books 검색조건과 페이지 구현 정보를 담고 있는 Beans
     * @return 조회 결과에 대한 컬렉션
     * @throws Exception
     */
    public List<Books> getBooksList(Books input) throws Exception;

    /**
     * 도서 데이터가 저장되어 있는 갯수 조회
     * @param Books 검색조건을 담고 있는 Beans
     * @return int
     * @throws Exception
     */
    public int getBooksCount(Books input) throws Exception;

    /**
     * 도서 데이터 등록하기
     * @param Books 저장할 정보를 담고 있는 Beans
     * @retrun int
     * @throws Exception
     */
    public int addBooks(Books input) throws Exception;
    public int addBooks2(Users_books input) throws Exception;

    /**
     * 도서 데이터 수정하기
     * @param Books 수정할 정보를 담고 있는 Beans
     * @retrun int
     * @throws Exception
     */
    public int editBooks(Books input) throws Exception;

    /**
     * 도서 데이터 삭제하기
     * @param Books 삭제할 도서의 일련번호를 담고 있는 Beans
     * @return int
     * @throws Exception
     */
    public int deleteBooks(Books input) throws Exception;
}
