package study.spring.hellobook.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import study.spring.hellobook.helper.PageData;
import study.spring.hellobook.helper.RegexHelper;
import study.spring.hellobook.helper.WebHelper;
import study.spring.hellobook.model.Books;
import study.spring.hellobook.model.Users_books;
import study.spring.hellobook.service.BooksService;
import study.spring.hellobook.service.UsersService;

@RestController
public class BooksRestController {
    /** WebHelper 주입 */
    // --> import org.springframework.beans.factory.annotation.Autowired;
    // --> import study.spring.hellobook.helper.WebHelper;
    @Autowired  WebHelper webHelper;

    /** RegexHelper 주입 */
    // --> import study.spring.hellobook.helper.RegexHelper;
    @Autowired  RegexHelper regexHelper;

    /** Service 패턴 구현체 주입 */
    // --> import study.spring.hellobook.service.BooksService;
    @Autowired  BooksService booksService;
    
    @Autowired  UsersService userService;

    /** 목록 페이지 */
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public Map<String, Object> get_list(
            // 검색어
            @RequestParam(value="keyword", required=false) String keyword,
            // 페이지 구현에서 사용할 현재 페이지 번호
            @RequestParam(value="page", defaultValue="1") int nowPage) {

        /** 1) 페이지 구현에 필요한 변수값 생성 */
        int totalCount = 0;              // 전체 게시글 수
        int listCount  = 10;             // 한 페이지당 표시할 목록 수
        int pageCount  = 5;              // 한 그룹당 표시할 페이지 번호 수

        /** 2) 데이터 조회하기 */
        // 조회에 필요한 조건값(검색어)를 Beans에 담는다.
        Books input = new Books();
        input.setTitle(keyword);

        List<Books> output = null;   // 조회결과가 저장될 객체
        PageData pageData = null;        // 페이지 번호를 계산한 결과가 저장될 객체

        try {
            // 전체 게시글 수 조회
            totalCount = booksService.getBooksCount(input);
            // 페이지 번호 계산 --> 계산결과를 로그로 출력될 것이다.
            pageData = new PageData(nowPage, totalCount, listCount, pageCount);

            // SQL의 LIMIT절에서 사용될 값을 Beans의 static 변수에 저장
            Books.setOffset(pageData.getOffset());
            Books.setListCount(pageData.getListCount());

            // 데이터 조회하기
            output = booksService.getBooksList(input);
        } catch (Exception e) {
            return webHelper.getJsonError(e.getLocalizedMessage());
        }

        /** 3) JSON 출력하기 */
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("keyword", keyword);
        data.put("item", output);
        data.put("meta", pageData);

        return webHelper.getJsonData(data);
    }

    /** 상세 페이지 */
    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public Map<String, Object> get_item(@PathVariable("id") int id) {

        /** 1) 데이터 조회하기 */
        // 데이터 조회에 필요한 조건값을 Beans에 저장하기
        Books input = new Books();
        input.setId(id);

        // 조회결과를 저장할 객체 선언
        Books output = null;

        try {
            // 데이터 조회
            output = booksService.getBooksItem(input);
        } catch (Exception e) {
            return webHelper.getJsonError(e.getLocalizedMessage());
        }

        /** 2) JSON 출력하기 */
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("item", output);

        return webHelper.getJsonData(data);
    }

    /** 작성 폼에 대한 action 페이지 */
    @RequestMapping(value = "/books", method = RequestMethod.POST)
    public Map<String, Object> post(HttpServletRequest request,
    		 @RequestParam(value="id", defaultValue = "0") int user_id,//사용자의 ID
    		 @RequestBody Books books ) {
    	// 로그인 여부 확인 -> 로그인 중 일때 : id!=0 / 로그인 하지 않았을때 : id ==0
    		HttpSession session = request.getSession();
    			if (session.getAttribute("my_session") != null
    					) {
    			user_id = (int) session.getAttribute("my_session");
    				}
        /** 1) 사용자가 입력한 파라미터에 대한 유효성 검사 */
        // 일반 문자열 입력 컬럼 --> String으로 파라미터가 선언되어 있는 경우는 값이 입력되지 않으면 빈 문자열로 처리된다.
        if (!regexHelper.isValue(books.getTitle()))    { return webHelper.getJsonWarning("도서 이름을 입력하세요."); }
        if (!regexHelper.isValue(books.getWriter()))   { return webHelper.getJsonWarning("도서 지은이를 입력하세요."); }
        if (!regexHelper.isValue(books.getPub())) 		{ return webHelper.getJsonWarning("출판사을 입력하세요."); }
        
        /** 2) 데이터 저장하기 */
        // 저장할 값들을 Beans에 담는다.
        Books input = new Books();
        input.setTitle(books.getTitle());
        input.setWriter(books.getWriter());
        input.setGenre(books.getGenre());
        input.setPub(books.getPub());
        input.setDetail(books.getDetail());
        input.setImg(books.getImg());
        input.setCreated_at(books.getCreated_at());
        
        // input.setUpdated_at(books.getUpdated_at());        
        // input.setIsrent(books.getIsrent());
        // input.setHide(books.getHide());


        // 저장된 결과를 조회하기 위한 객체
        Books output = null;

        try {
            // 데이터 저장
            // --> 데이터 저장에 성공하면 파라미터로 전달하는 input 객체에 PK값이 저장된다.
            booksService.addBooks(input);

            // 데이터 조회
            output = booksService.getBooksItem(input);
        } catch (Exception e) {
            return webHelper.getJsonError(e.getLocalizedMessage());
        }
        
        int book_id = output.getId();

        /** 3) 결과를 확인하기 위한 JSON 출력 */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("item", output);
        
        
        //저장 객체 생성 
       	Users_books input2 = new Users_books();
       	input2.setUser_id(user_id);
       	input2.setBook_id(book_id);
       	// 데이터 저장 Users_books
       	try {
            
            booksService.addBooks2(input2);
        } catch (Exception e) {
            return webHelper.getJsonError(e.getLocalizedMessage());
        }
        
        
        return webHelper.getJsonData(map);
    }

    /** 수정 폼에 대한 action 페이지 */
    @RequestMapping(value = "/books", method = RequestMethod.PUT)
    public Map<String, Object> put(
    		@RequestBody Books books)  {

        /** 1) 사용자가 입력한 파라미터 유효성 검사 */
        if (!regexHelper.isValue(books.getTitle()))     { return webHelper.getJsonWarning("도서 이름을 입력하세요."); }
        if (!regexHelper.isValue(books.getWriter()))   { return webHelper.getJsonWarning("도서 지은이를 입력하세요."); }
        if (!regexHelper.isValue(books.getPub())) { return webHelper.getJsonWarning("출판사를 입력하세요."); }

        /** 2) 데이터 수정하기 */
        // 수정할 값들을 Beans에 담는다.
        Books input = new Books();
        input.setId(books.getId());
        input.setTitle(books.getTitle());
        input.setWriter(books.getWriter());
        input.setGenre(books.getGenre());
        input.setPub(books.getPub());
        input.setDetail(books.getDetail());
        input.setImg(books.getImg());
        input.setUpdated_at(books.getUpdated_at());
        input.setIsrent(books.getIsrent());
        input.setHide(books.getHide());

        // 수정된 결과를 조회하기 위한 객체
        Books output = null;

        try {
            // 데이터 수정
            booksService.editBooks(input);
            // 수정 결과 조회
            output = booksService.getBooksItem(input);
        } catch (Exception e) {
            return webHelper.getJsonError(e.getLocalizedMessage());
        }

        /** 3) 결과를 확인하기 위한 JSON 출력 */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("item", output);
        return webHelper.getJsonData(map);
    }
    

    /** 삭제 처리 */
    @RequestMapping(value = "/books", method = RequestMethod.DELETE)
    public Map<String, Object> delete(
            @RequestParam(value="id", defaultValue="0") int id) {

        /** 1) 파라미터 유효성 검사 */
        // 이 값이 존재하지 않는다면 데이터 삭제가 불가능하므로 반드시 필수값으로 처리해야 한다.
        if (id == 0) {
            return webHelper.getJsonWarning("도서 번호가 없습니다.");
        }

        /** 2) 데이터 삭제하기 */
        // 데이터 삭제에 필요한 조건값을 Beans에 저장하기
        Books input = new Books();
        input.setId(id);

        try {
            booksService.deleteBooks(input); // 데이터 삭제
        } catch (Exception e) {
            return webHelper.getJsonError(e.getLocalizedMessage());
        }

        /** 3) 결과를 확인하기 위한 JSON 출력 */
        // 확인할 대상이 삭제된 결과값만 OK로 전달
        return webHelper.getJsonData();
    }
}