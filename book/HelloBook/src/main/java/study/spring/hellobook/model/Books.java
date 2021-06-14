package study.spring.hellobook.model;

import lombok.Data;

@Data
public class Books {
    // 1) 기본 컬럼
    /** 도서번호 (PK) */
    private int id;
    /** 제목 */
    private String title;
    /** 지은이 */
    private String writer;
    /** 출판사*/
    private String pub;
    /** 등록일시 */
    private String created_at;
    /** 수정일시 */
    private String updated_at;
    /** 대여가능여부 */
    private boolean	isrent;
    


    // 3) 페이지 구현을 위한 static 변수
    /** LIMIT 절에서 사용할 조회 시작 위치 */
    private static int offset;

    /** LIMIT 절에서 사용할 조회할 데이터 수 */
    private static int listCount;

    public static int getOffset() {
        return offset;
    }

    public static void setOffset(int offset) {
        Books.offset = offset;
    }

    public static int getListCount() {
        return listCount;
    }

    public static void setListCount(int listCount) {
        Books.listCount = listCount;
    }
}