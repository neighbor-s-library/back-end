package study.spring.hellobook.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;

import study.spring.hellobook.helper.MailHelper;
import study.spring.hellobook.helper.RegexHelper;
import study.spring.hellobook.helper.WebHelper;
import study.spring.hellobook.model.Auth;
import study.spring.hellobook.model.Users;
import study.spring.hellobook.service.UsersService;

@RestController
public class UsersRestController {

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
	UsersService userService;

	/** Spring Security 주입 */
	@Autowired
	BCryptPasswordEncoder pwdEncoder;

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/** "/프로젝트이름" 에 해당하는 ContextPath 변수 주입 */
	@Value("#{servletContext.contextPath}")
	String contextPath;

	/** 아이디 중복 검사 */
	@RequestMapping(value = "/users/id_check.do", method = RequestMethod.POST)
	public Map<String, Object> id_check(Model model, @RequestParam(value = "email", defaultValue = "") String email) {

		/** 1) 아이디 조회를 위해 Bean에 담는다 */
		Users input = new Users();
		input.setEmail(email);

		// 조회된 결과를 확인하기 위한 객체
		int output = 0;

		try {
			output = userService.getIdItem(input);
		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		/** 2) JSON 출력하기 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("item", output);

		return webHelper.getJsonData(data);

	}

	/** 회원가입 작성 폼에 대한 action 페이지 */
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public Map<String, Object> add_user(Model model, @RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "address", defaultValue = "") String address,
			@RequestParam(value = "nickname", defaultValue = "") String nickname,
			@RequestParam(value = "inputpw", defaultValue = "") String inputpw,
			@RequestParam(value = "tel", defaultValue = "") String tel) {
		// POST 방식으로 Join_ok에서 Join cfm

		/** 1) 사용자가 입력한 파라미터 유효성 검사 */
		if (!regexHelper.isValue(email)) {
			return webHelper.getJsonWarning("이메일을 입력하세요.");
		}
		if (!regexHelper.isValue(address)) {
			return webHelper.getJsonWarning("주소를 입력하세요.");
		}
		if (!regexHelper.isValue(nickname)) {
			return webHelper.getJsonWarning("닉네임을 입력하세요.");
		}
		if (!regexHelper.isValue(inputpw)) {
			return webHelper.getJsonWarning("비밀번호를 입력하세요.");
		}
		if (!regexHelper.isNum(tel)) {
			return webHelper.getJsonWarning("전화번호를 올바른 형식에 맞게 입력하세요.");
		}

		/** 2) 데이터 저장하기 */
		// 저장할 값들을 Beans에 담는다.
		Users input = new Users();
		input.setEmail(email);
		input.setAddress(address);
		input.setNickname(nickname);

		// 비밀번호 암호화
		String pw = pwdEncoder.encode(inputpw);

		// 저장된 결과를 조회하기 위한 객체
		Users output = null;

		try {

			int idCheck = 0;
			idCheck = userService.getIdItem(input);
			if (idCheck != 1) {
				// 데이터 저장
				userService.addUser(input);

				// 비밀번호, 전화번호 (개인정보) 저장
				int user_id = input.getId();
				Auth input2 = new Auth();
				input2.setUser_id(user_id);
				input2.setPw(pw);
				input2.setTel(tel);
				userService.addUser2(input2);

				// 데이터 조회
				output = userService.getUserItem(input);
			} else {
				return webHelper.getJsonError("이미 존재하는 이메일 입니다.");
			}

		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		/** 3) 결과를 확인하기 위한 페이지 연동 */
		// 저장 결과를 확인하기 위해 데이터 저장 시 생성된 PK값을 상세 페이지로 전달해야 한다.
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item", output);
		return webHelper.getJsonData(map);
	}

	/** 로그인 아이디, 패스워드 확인 */
	@RequestMapping(value = "/users/login_ok.do", method = RequestMethod.POST)
	public Map<String, Object> login_post(Model model, HttpServletRequest request,
			@RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "userpwd", defaultValue = "") String userpwd) {

		/** 사용자가 입력한 파라미터 유효성 검사 */
		if (!regexHelper.isValue(email)) {
			return webHelper.getJsonWarning("아이디를 입력하세요.");
		}
		if (!regexHelper.isValue(userpwd)) {
			return webHelper.getJsonWarning("비밀번호를 입력하세요.");
		}

		// 이메일 확인 -> 이메일 정보를 담은 빈즈로 id 확인, id로 pw 찾기 -> 사용자가 입력한 pw 암호화
		// 두개의 pw 대조 -> 일치하면 Users 정보 확인가능, 세션에 정보 저장

		/** 입력값 일치 확인하기 */
		// 저장할 값들을 Beans에 담는다.
		Users input = new Users();
		input.setEmail(email);
		Users idoutput = new Users();

		try {
			// 이메일 정보를 담은 input빈즈를 파라미터로 주고 id 값을 확인
			userService.getUserItem2(input);

			// 결과 조회
			idoutput = userService.getUserItem2(input);

			if (idoutput == null) {
				return webHelper.getJsonError("존재하지 않는 이메일 입니다.");
			}

		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		// email로 찾은 id
		int user_id = idoutput.getId();

		// 비밀번호를 찾을 id값을 넣은 빈즈
		Auth userIdInput = new Auth();
		userIdInput.setUser_id(user_id);

		try {

			userIdInput = userService.getUsersInfo(userIdInput);

		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		// DB에 저장되어있는 비밀번호
		String dbpwd = userIdInput.getPw();

		/** request 객체를 사용해서 세션 객체 만들기 */
		HttpSession session = request.getSession();

		// 입력한 비밀번호와 DB에서 가져온 비밀번호가 일치할 경우 데이터를 조회한다.
		if (pwdEncoder.matches(userpwd, dbpwd)) {

			// 세션 저장 처리
			session.setAttribute("my_session", userIdInput.getUser_id());

		} else {
			/** 조회에 실패한 경우(DB에 데이터가 존재하지 않는 경우) */
			// 세션 삭제
			session.removeAttribute("my_session");
			return webHelper.getJsonError("비밀번호가 잘못되었습니다.");
		}

		// 결과를 저장할 빈즈
		Users output = new Users();

		try {
			// 사용자 번호로 사용자 정보 조회
			output = userService.getUserItem(idoutput);

		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());

		}

		/** 3)JSON 출력하기 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("item", output);
		return webHelper.getJsonData(data);
	}

	/** 로그아웃 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	/** 개인 정보 수정 */
	@RequestMapping(value = "/users/usersRevise_ok", method = RequestMethod.PUT)
	public Map<String, Object> users_revise(Model model, HttpServletRequest request,
			@RequestParam(value = "id", defaultValue = "0") int id,
			@RequestParam(value = "address", defaultValue = "") String address,
			@RequestParam(value = "nickname", defaultValue = "") String nickname) {

		/** 파라미터에 대한 유효성 검사 */

		// 로그인 여부 확인 -> 로그인 중 일때 : id!=0 / 로그인 하지 않았을때 : id ==0
		HttpSession session = request.getSession();
		if (session.getAttribute("my_session") != null) {
			id = (int) session.getAttribute("my_session");
		}

		if (id == 0) {
			return webHelper.getJsonWarning("사용자 번호가 없습니다.");
		}
		if (!regexHelper.isValue(address)) {
			return webHelper.getJsonWarning("주소를 입력해주세요.");
		}
		if (!regexHelper.isValue(nickname)) {
			return webHelper.getJsonWarning("닉네임을 입력해주세요.");
		}

		/** 데이터 조회하기 */
		Users input = new Users();

		input.setId(id);
		input.setNickname(nickname);
		input.setAddress(address);

		Users output = null;

		try {
			// 데이터 수정
			userService.usersRevise(input);

			// 수정 결과 조회
			output = userService.getUserItem(input);

		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		/** 결과를 확인하기 위한 JSON 출력 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item", output);
		return webHelper.getJsonData(map);

	}

	/** 개인 정보 수정 (비밀번호, 전화번호) */
	@RequestMapping(value = "/users/usersInfoRevise_ok", method = RequestMethod.PUT)
	public Map<String, Object> usersInfo_revise(Model model, HttpServletRequest request,
			@RequestParam(value = "id", defaultValue = "0") int id,
			@RequestParam(value = "inputpw", defaultValue = "") String inputpw,
			@RequestParam(value = "tel", defaultValue = "") String tel) {

		/** 파라미터에 대한 유효성 검사 */

		// 로그인 여부 확인 -> 로그인 중 일때 : id!=0 / 로그인 하지 않았을때 : id ==0
		HttpSession session = request.getSession();
		if (session.getAttribute("my_session") != null) {
			id = (int) session.getAttribute("my_session");
		}

		if (id == 0) {
			return webHelper.getJsonWarning("사용자 번호가 없습니다.");
		}
		if (!regexHelper.isValue(inputpw)) {
			return webHelper.getJsonWarning("새로운 비밀번호를 입력해주세요.");
		}
		if (!regexHelper.isValue(tel)) {
			return webHelper.getJsonWarning("전화번호를 입력해주세요.");
		}

		/** 데이터 조회하기 */
		Auth input = new Auth();

		input.setUser_id(id);
		// 비밀번호 암호화
		String pw = pwdEncoder.encode(inputpw);
		input.setPw(pw);
		input.setTel(tel);

		Users output = null;

		try {
			// 데이터 수정
			userService.usersInfoRevise(input);

			int userid = input.getUser_id();
			Users updateinput = new Users();
			updateinput.setId(userid);

			// 수정 결과 조회
			output = userService.getUserItem(updateinput);

		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		/** 결과를 확인하기 위한 JSON 출력 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item", output);
		return webHelper.getJsonData(map);

	}

	// 비밀번호 찾기
	@RequestMapping(value = "/users/pwfind_ok.do", method = RequestMethod.POST)
	public Map<String, Object> pw_find(Model model, @RequestParam(value = "email", defaultValue = "") String email) {

		if (!regexHelper.isValue(email)) {
			return webHelper.getJsonWarning("이메일을 입력하세요.");
		}

		/** 1) 데이터 조회하기 */
		// 데이터 조회에 필요한 조건을 Beans에 저장하기
		Users input = new Users();
		input.setEmail(email);

		// 조회결과를 저장할 객체 선언
		Users output = null;

		try {
			// 데이터 조회
			output = userService.getUserItem2(input);

			// 이메일 존재 하면 메일 발송 처리
			if (output != null) {
				try {
					// 이메일 제목
					String subject = "HelloBook 비밀번호 재설정을 위한 메세지 입니다.";
					// 비밀번호 수정 페이지 URL
					String url = "비밀번호 수정 URL(현재 임시)";
					// 사용자 번호
					int param_userno = output.getId();

					String pwrevise_url = url + "?" + "id=" + param_userno;

					// 전송할 이메일 내용
					String content = "<div style = 'width : 75%; border: 35px solid #FBEAE9; margin:auto; padding:30px;'>"
							+ "<h2> 안녕하세요. HelloBook 입니다 </h2>" + "<br />"
							+ "<p> 해당 메일은 비밀번호 재설정 요청에 의해 전송된 이메일 입니다. <p>" + "<br />"
							+ "<p> 고객님께서 요청하지 않으신 내용이라면 HelloBook에 접속하여 비밀번호를 변경해주시고, 고객센터에 문의 해주시기 바랍니다.</p>"
							+ "<br />" + "<p> 비밀번호 변경을 요청하셨다면 아래 버튼을 클릭하시면 비밀번호 변경이 가능한 페이지로 이동합니다.</p>" + "<br />"
							+ "<a type= 'button' style ='display:block; width:140px; height: 32px; color:#fff; "
							+ "font-size: 12px; font-weight:bold; background-color: #FBD6AB; margin:auto; "
							+ "margin-top: 20px; border-radius:25px; text-align: center; padding-top: 15px; text-decoration: none;' href='"
							+ pwrevise_url + "'>비밀번호 변경하기</a>" + "</div>";
					// sendMail() 메서드 선언시 throws 를 정의했기 때문에 예외처리가 요구된다.
					mailHelper.sendMail(output.getEmail(), subject, content);
				} catch (Exception e) {
					return webHelper.getJsonError(e.getLocalizedMessage());
				}
			}
		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}
		/** 3)JSON 출력하기 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("item", output);
		return webHelper.getJsonData(data);
	}

}
