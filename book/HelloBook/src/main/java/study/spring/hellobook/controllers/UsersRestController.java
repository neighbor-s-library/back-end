/*
 * package study.spring.hellobook.controllers;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.web.bind.annotation.RestController; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 * 
 * import study.spring.hellobook.helper.RegexHelper; import
 * study.spring.hellobook.helper.WebHelper; import
 * study.spring.hellobook.service.UsersService;
 * 
 * @RestController public class UsersRestController {
 * 
 *//** WebHelper 주입 */
/*
 * @Autowired WebHelper webHelper;
 * 
 *//** RegexHelper 주입 */
/*
 * @Autowired RegexHelper regexHelper;
 * 
 *//** Service 패턴 구현체 주입 */
/*
 * @Autowired UsersService userService;
 * 
 *//** Spring Security 주입 */
/*
 * @Autowired BCryptPasswordEncoder pwdEncoder;
 * 
 *//** "/프로젝트이름" 에 해당하는 ContextPath 변수 주입 *//*
											 * @Value("#{servletContext.contextPath}") String contextPath; }
											 */