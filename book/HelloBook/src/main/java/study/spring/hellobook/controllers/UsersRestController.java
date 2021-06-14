package study.spring.hellobook.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;

import study.spring.hellobook.helper.RegexHelper;
import study.spring.hellobook.helper.WebHelper;
import study.spring.hellobook.model.Auth;
import study.spring.hellobook.model.Users;
import study.spring.hellobook.service.UsersService;

@RestController
public class UsersRestController {

	/** WebHelper 주입 */
    @Autowired  WebHelper webHelper;

    /** RegexHelper 주입 */
    @Autowired  RegexHelper regexHelper;
    
    /** Service 패턴 구현체 주입 */
    @Autowired  UsersService userService;
	
	/** Spring Security 주입 */
    @Autowired BCryptPasswordEncoder pwdEncoder;
    
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder()
    {
      return new BCryptPasswordEncoder();
    }

    
    /** "/프로젝트이름" 에 해당하는 ContextPath 변수 주입 */
    @Value("#{servletContext.contextPath}")
    String contextPath;
    
    /**아이디 중복 검사*/
	@RequestMapping(value="/users/id_check.do", method=RequestMethod.POST)
	public Map<String, Object>id_check(Model model,
			@RequestParam(value = "email", defaultValue="") String email){

		/**1) 아이디 조회를 위해 Bean에 담는다*/
			Users input = new Users();
			input.setEmail(email);

		//조회된 결과를 확인하기 위한 객체
			int output = 0;

			try {
				output = userService.getIdItem(input);
			}catch (Exception e) {
	            return webHelper.getJsonError(e.getLocalizedMessage());
	        }

	        /** 2) JSON 출력하기 */
	        Map<String, Object> data = new HashMap<String, Object>();
	        data.put("item", output);

	        return webHelper.getJsonData(data);

	}
	
	/** 회원가입 작성 폼에 대한 action 페이지*/
	@RequestMapping(value="/users", method=RequestMethod.POST)
	public Map<String, Object> add_user(Model model,
			@RequestParam(value="email", defaultValue="") String email,
			@RequestParam(value="address", defaultValue="") String address,
			@RequestParam(value="nickname", defaultValue="") String nickname,
			@RequestParam(value="inputpw", defaultValue="") String inputpw,
			@RequestParam(value="tel", defaultValue="") String tel
			) {
		//POST 방식으로 Join_ok에서 Join cfm

		/**1) 사용자가 입력한 파라미터 유효성 검사*/
		if(!regexHelper.isValue(email))				{return webHelper.getJsonWarning("이메일을 입력하세요.");}
		if(!regexHelper.isValue(address))			{return webHelper.getJsonWarning("주소를 입력하세요.");}
		if(!regexHelper.isValue(nickname))			{return webHelper.getJsonWarning("닉네임을 입력하세요.");}
		if(!regexHelper.isValue(inputpw))				{return webHelper.getJsonWarning("비밀번호를 입력하세요.");}
		if(!regexHelper.isNum(tel))					{return webHelper.getJsonWarning("전화번호를 올바른 형식에 맞게 입력하세요.");}

		/**2) 데이터 저장하기*/
		//저장할 값들을 Beans에 담는다.
		Users input = new Users();
		input.setEmail(email);
		input.setAddress(address);
		input.setNickname(nickname);
		
		//비밀번호 암호화
		String pw = pwdEncoder.encode(inputpw);

		// 저장된 결과를 조회하기 위한 객체
		Users output = null;

		try {
			
			int idCheck = 0;
			idCheck = userService.getIdItem(input);
			if(idCheck != 1) {
				//데이터 저장
				userService.addUser(input);
				
				//비밀번호, 전화번호 (개인정보) 저장
				int user_id = input.getId();
				Auth input2 = new Auth();
				input2.setUser_id(user_id);
				input2.setPw(pw);
				input2.setTel(Integer.parseInt(tel));
				userService.addUser2(input2);

				//데이터 조회
				output = userService.getUserItem(input);
			}else {
				return webHelper.getJsonError("이미 존재하는 이메일 입니다.");
			}

		}catch(Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		/**3) 결과를 확인하기 위한 페이지 연동*/
		//저장 결과를 확인하기 위해 데이터 저장 시 생성된 PK값을 상세 페이지로 전달해야 한다.
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item", output);
		return webHelper.getJsonData(map);
	}
}
