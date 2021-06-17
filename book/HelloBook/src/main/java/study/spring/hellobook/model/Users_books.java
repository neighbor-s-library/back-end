package study.spring.hellobook.model;

import lombok.Data;

@Data
public class Users_books {
    // 1) 기본 컬럼
    /** 도서번호 (PK) */
    private int user_id;
    
    /** 제목 */
   private int book_id;
    


    // 3) 페이지 구현을 위한 static 변수
    /** LIMIT 절에서 사용할 조회 시작 위치 */
    private static int offset;

    /** LIMIT 절에서 사용할 조회할 데이터 수 */
    private static int listCount;

    public static int getOffset() {
        return offset;
    }

    public static void setOffset(int offset) {
        Users_books.offset = offset;
    }

    public static int getListCount() {
        return listCount;
    }

    public static void setListCount(int listCount) {
        Users_books.listCount = listCount;
    }
}