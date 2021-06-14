package study.spring.hellobook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import study.spring.hellobook.helper.RegexHelper;
import study.spring.hellobook.helper.WebHelper;
import study.spring.hellobook.service.UsersService;

@Controller
public class UsersAjaxController {
	/** Webhelper 주입 */
	// -> import org.springframework.beans.factory.annotation.Autowired;
	// -> import study.spring.springhelper.helper.WebHelper;
	@Autowired WebHelper webHelper;
	
	/** RegexHelper 주입 */
	// -> import study.spring.springhelper.helper.RegexHelper;
	@Autowired RegexHelper regexHelper;
	
	/** Service 패턴 구현체 주입 */
	// -> import study.spring.springhelper.service.ProfessorService;
	@Autowired UsersService userService;
	
	/** "/프로젝트이름" 에 해당하는 ContextPath 변수 주입 */
	// -> import org.springframework.beans.factory.annotation.Value;
	@Value("#{servletContext.contextPath}")
	String contextPath;
	
	

}
