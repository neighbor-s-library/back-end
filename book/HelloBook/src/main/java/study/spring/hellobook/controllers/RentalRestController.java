package study.spring.hellobook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import study.spring.hellobook.helper.MailHelper;
import study.spring.hellobook.helper.RegexHelper;
import study.spring.hellobook.helper.WebHelper;
import study.spring.hellobook.service.RentalService;

@RestController
public class RentalRestController {

	/** WebHelper 주입 */
	@Autowired
	WebHelper webHelper;

	/** RegexHelper 주입 */
	@Autowired
	RegexHelper regexHelper;

	/** MailHlper 주입 */
	@Autowired
	MailHelper mailHelper;

	/** Service 패턴 구현체 주입 */
	@Autowired
	RentalService rentalService;
	
	/** "/프로젝트이름" 에 해당하는 ContextPath 변수 주입 */
	@Value("#{servletContext.contextPath}")
	String contextPath;

	//대여
	
	//대여완료
}
