package study.spring.hellobook.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import study.spring.hellobook.helper.PageData;
import study.spring.hellobook.helper.RegexHelper;
import study.spring.hellobook.helper.WebHelper;
import study.spring.hellobook.model.Books;
import study.spring.hellobook.service.BooksService;

@Controller
public class BooksController {
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

    /** "/프로젝트이름" 에 해당하는 ContextPath 변수 주입 */
    // --> import org.springframework.beans.factory.annotation.Value;
    @Value("#{servletContext.contextPath}")
    String contextPath;



    /** 목록 페이지 */
    @RequestMapping(value="/books/list.do", method = RequestMethod.GET)
    public ModelAndView list(Model model,
            // 검색어
            @RequestParam(value="keyword", required=false) String keyword,
            // 페이지 구현에서 사용할 현재 페이지 번호
            @RequestParam(value="page", defaultValue="1") int nowPage) {

        /** 1) 페이지 구현에 필요한 변수값 생성 */
        int totalCount = 0;     // 전체 게시글 수
        int listCount = 10;     // 한 페이지당 표시할 목록 수
        int pageCount = 5;      // 한 그룹당 표시할 페이지 번호 수

        /** 2) 데이터 조회하기 */
        // 조회에 필요한 조건값(검색어)를 Beans에 담는다.
        Books input = new Books();
        input.setTitle(keyword);

        List<Books> output = null;      // 조회결과가 저장될 객체
        PageData pageData = null;           // 페이지 번호를 계산한 결과가 저장될 객체

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
            return webHelper.redirect(null, e.getLocalizedMessage());
        }

        /** 3) View 처리 */
        model.addAttribute("keyword", keyword);
        model.addAttribute("output", output);
        model.addAttribute("pageData", pageData);

        return new ModelAndView("books/list");

    }

    /** 상세 페이지 */
    @RequestMapping(value="/books/view.do", method = RequestMethod.GET)
    public ModelAndView view(Model model,
            @RequestParam(value="id", defaultValue="0") int id) {

        /** 1) 유효성 검사 */
        // 이 값이 존재하지 않는다면 데이터 조회가 불가능하므로 반드시 필수값으로 처리해야 한다.
        if (id == 0) {
            return webHelper.redirect(null, "도서번호가 없습니다.");
        }

        /** 2) 데이터 조회하기 */
        // 데이터 조회에 필요한 조건값을 Beans에 저장하기
        Books input = new Books();
        input.setId(id);

        // 조회결과를 저장할 객체 선언
        Books output = null;

        try {
            // 데이터 조회
            output = booksService.getBooksItem(input);
        } catch (Exception e) {
            return webHelper.redirect(null, e.getLocalizedMessage());
        }

        /** 3) View 처리 */
        model.addAttribute("output", output);

        return new ModelAndView("books/view");
    }

    /** 작성 폼 페이지 */
    @RequestMapping(value="/books/add.do", method = RequestMethod.GET)
    public ModelAndView add(Model model) {

        /** 학과 목록 조회하기 */
        // 조회결과를 저장할 객체 선언
        //List<Department> output = null;
		/*
		 * try { // 데이터 조회 --> 검색조건 없이 모든 학과 조회 output =
		 * departmentService.getDepartmentList(null); } catch (Exception e) { return
		 * webHelper.redirect(null, e.getLocalizedMessage()); }
		 * 
		 * // View에 추가 model.addAttribute("output", output);
		 나중 : 장르 리스트를 꺼내야?*/
    

        return new ModelAndView("books/add");

    }

    /** 작성 폼에 대한 action 페이지 */
    @RequestMapping(value="/books/add_ok.do", method = RequestMethod.POST)
    public ModelAndView add_ok(Model model,
            @RequestParam(value="title", defaultValue="") String title,
            @RequestParam(value="writer", defaultValue="") String writer,
            @RequestParam(value="pub", defaultValue="") String pub,
            @RequestParam(value="created_at", defaultValue="") String created_at,
            @RequestParam(value="updated_at", defaultValue="") String updated_at,
            @RequestParam(value="Isrent", defaultValue="") int isrent ) {

        /** 1) 사용자가 입력한 파라미터 유효성 검사 */
        // 일반 문자열 입력 컬럼 --> String으로 파라미터 가 선언되어 있는 값이 입력되지 않으면 빈 문자열로 처리된다.
        if (!regexHelper.isValue(title)) { return webHelper.redirect(null, "도서 이름을 입력하세요."); }
        if (!regexHelper.isKor(title)) { return webHelper.redirect(null, "도서 이름은 한글만 가능합니다."); }
        if (!regexHelper.isValue(writer)) { return webHelper.redirect(null, "도서 지은이를 입력하세요."); }
        if (!regexHelper.isEngNum(writer)) { return webHelper.redirect(null, "도서 지은이는 영어와 숫자로만 가능합니다."); }
        if (!regexHelper.isValue(pub)) { return webHelper.redirect(null, "출판사을 입력하세요."); }
        if (!regexHelper.isValue(created_at)) { return webHelper.redirect(null, "등록일시을 입력하세요."); }


        /** 2) 데이터 저장하기 */
        // 저장할 값들을 Beans에 담는다.
        Books input = new Books();
        input.setTitle(title);
        input.setWriter(writer);
        input.setPub(pub);
        input.setCreated_at(created_at);
        input.setUpdated_at(updated_at);
        input.setIsrent(isrent);

        try {
            // 데이터 저장
            // --> 데이터 저장에 성공하면 파라미터로 전달하는 input 객체에 PK값이 저장된다.
            booksService.addBooks(input);
        } catch (Exception e) {
            return webHelper.redirect(null, e.getLocalizedMessage());
        }

        /** 3) 결과를 확인하기 위한 페이지 이동 */
        // 저장 결과를 확인하기 위해서 데이터 저장시 생성된 PK값을 상세 페이지로 전달해야 한다.
        String redirectUrl = contextPath + "/books/view.do?id=" + input.getId();
        return webHelper.redirect(redirectUrl, "저장되었습니다.");
    }

    /** 수정 폼 페이지 */
    @RequestMapping(value="/books/edit.do", method = RequestMethod.GET)
    public ModelAndView edit(Model model,
            @RequestParam(value="id", defaultValue="0") int id) {

//        /** 1) 파라미터 유효성 검사 */
//        // 이 값이 존재하지 않는다면 데이터 조회가 불가능하므로 반드시 필수값으로 처리해야 한다.
//        if(id == 0) {
//            return webHelper.redirect(null, "도서번호가 없습니다.");
//        }
//
//        /** 2) 데이터 조회하기 */
//        // 데이터 조회에 필요한 조건값을 Beans에 저장하기
//        Books input = new Books();
//        input.setId(id);
//
//        // 도서 조회결과를 저장할 객체 선언
//        Books output = null;
//
//        // 학과목록을 선택할 수 있는 드롭다운을 위한 조회결과를 저장할 객체 선언
//        List<Department> deptList = null;
//
//        try {
//            // 도서 기본 정보 조회
//            output = booksService.getBooksItem(input);
//            // 드롭다운을 위한 학과목록 조회
//            deptList = departmentService.getDepartmentList(null);
//        } catch (Exception e) {
//            return webHelper.redirect(null, e.getLocalizedMessage());
//        }
//
//        /** 3) View 처리 */
//        model.addAttribute("output", output);
//        model.addAttribute("deptList", deptList);
        return new ModelAndView("books/edit");
    	// 나중에 디바트먼트 장르로 바꿔서 다시 도전
    }

    /** 수정 폼에 대한 action 페이지 */
    @RequestMapping(value="/books/edit_ok.do", method = RequestMethod.POST)
    public ModelAndView edit_ok(Model mode,
    		@RequestParam(value="title", defaultValue="") String title,
            @RequestParam(value="writer", defaultValue="") String writer,
            @RequestParam(value="pub", defaultValue="") String pub,
            @RequestParam(value="created_at", defaultValue="") String created_at,
            @RequestParam(value="updated_at", defaultValue="") String updated_at,
            @RequestParam(value="Isrent", defaultValue="") int isrent) {

        /** 1) 사용자가 입력한 파라미터 유효성 검사 */
        // 일반 문자열 입력 컬럼 --> String으로 파라미터가 선언되어 있는 경우는 값이 입력되지 않으면 빈 문자열로 처리된다.
        if (!regexHelper.isValue(title))     { return webHelper.redirect(null, "도서 이름을 입력하세요."); }
        if (!regexHelper.isKor(title))       { return webHelper.redirect(null, "도서 이름은 한글만 가능합니다."); }
        if (!regexHelper.isValue(writer))   { return webHelper.redirect(null, "도서 지은이를 입력하세요."); }
        if (!regexHelper.isEngNum(writer))  { return webHelper.redirect(null, "도서 지은이는 영어와 숫자로만 가능합니다."); }
        if (!regexHelper.isValue(pub)) { return webHelper.redirect(null, "출판사을 입력하세요."); }
        if (!regexHelper.isValue(created_at)) { return webHelper.redirect(null, "등록일시을 입력하세요."); }

        /** 2) 데이터 저장하기 */
        // 저장할 값들을 Beans에 담는다.
        Books input = new Books();
        input.setTitle(title);
        input.setWriter(writer);
        input.setPub(pub);
        input.setCreated_at(created_at);
        input.setUpdated_at(updated_at);
        input.setIsrent(isrent);

        try {
            // 데이터 저장
            // --> 데이터 저장에 성공하면 파라미터로 전달하는 input 객체에 PK값이 저장된다.
            booksService.editBooks(input);
        } catch (Exception e) {
            return webHelper.redirect(null, e.getLocalizedMessage());
        }

        /** 3) 결과를 확인하기 위한 페이지 이동 */
        // 저장 결과를 확인하기 위해서 데이터 저장시 생성된 PK값을 상세 페이지로 전달해야 한다.
        String redirectUrl = contextPath + "/books/view.do?id=" + input.getId();
        return webHelper.redirect(redirectUrl, "수정되었습니다.");
    }

    /** 삭제 처리 */
    @RequestMapping(value="/books/delete_ok.do", method = RequestMethod.GET)
    public ModelAndView delete_ok(Model model,
            @RequestParam(value="id", defaultValue="0") int id) {

        /** 1) 파라미터 유효성 검사 */
        // 이 값이 존재하지 않는다면 데이터 삭제가 불가능하므로 반드시 필수값으로 처리해야 한다.
        if (id == 0) {
            return webHelper.redirect(null, "도서 번호가 없습니다.");
        }

        /** 2) 데이터 삭제하기 */
        // 데이터 삭제에 필요한 조건값을 Beans에 저장하기
        Books input = new Books();
        input.setId(id);

        try {
            booksService.deleteBooks(input);    // 데이터 삭제
        } catch (Exception e) {
            return webHelper.redirect(null, e.getLocalizedMessage());
        }

        /** 3) 페이지 이동 */
        // 확인할 대상이 삭제된 상태이므로 목록 페이지로 이동
        return webHelper.redirect(contextPath + "/books/list.do", "삭제되었습니다.");
    }

}
