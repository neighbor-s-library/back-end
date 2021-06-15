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
public class BooksAjaxController {
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
    @RequestMapping(value="/books/list_ajax.do", method = RequestMethod.GET)
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

        return new ModelAndView("books/list_ajax");
    }

    /** 상세 페이지 */
    @RequestMapping(value="/books/view_ajax.do", method = RequestMethod.GET)
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
        return new ModelAndView("books/view_ajax");
    }

    /** 작성 폼 페이지 */
    @RequestMapping(value="/books/add_ajax.do", method = RequestMethod.GET)
    public ModelAndView add(Model model) {

//        /** 학과 목록 조회하기 */
//        // 조회결과를 저장할 객체 선언
//        List<Department> output = null;
//
//        try {
//            // 데이터 조회 --> 검색조건 없이 모든 학과 조회
//            output = departmentService.getDepartmentList(null);
//        } catch (Exception e) {
//            return webHelper.redirect(null, e.getLocalizedMessage());
//        }
//	나중에 장르랑
//        // View에 추가
//        model.addAttribute("output", output);

        return new ModelAndView("books/add_ajax");
    }

    /** 수정 폼 페이지 */
    @RequestMapping(value="/books/edit_ajax.do", method = RequestMethod.GET)
    public ModelAndView edit(Model model,
            @RequestParam(value="id", defaultValue="0") int id) {

        /** 1) 파라미터 유효성 검사 */
        // 이 값이 존재하지 않는다면 데이터 조회가 불가능하므로 반드시 필수값으로 처리해야 한다.
        if(id == 0) {
            return webHelper.redirect(null, "도서번호가 없습니다.");
        }

        /** 2) 데이터 조회하기 */
        // 데이터 조회에 필요한 조건값을 Beans에 저장하기
        Books input = new Books();
        input.setId(id);

        // 도서 조회결과를 저장할 객체 선언
        Books output = null;

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
//        } 나중에 장르랑

        /** 3) View 처리 */
        model.addAttribute("output", output);
//        model.addAttribute("deptList", deptList);
        return new ModelAndView("books/edit_ajax");
    }
}
