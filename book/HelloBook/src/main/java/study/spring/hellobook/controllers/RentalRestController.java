package study.spring.hellobook.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import study.spring.hellobook.helper.MailHelper;
import study.spring.hellobook.helper.RegexHelper;
import study.spring.hellobook.helper.WebHelper;
import study.spring.hellobook.model.BookList;
import study.spring.hellobook.model.Rental;
import study.spring.hellobook.model.Users;
import study.spring.hellobook.service.JwtService;
import study.spring.hellobook.service.RentalService;
import study.spring.hellobook.service.UsersService;

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
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	JwtService jwtService;
	
	/** "/프로젝트이름" 에 해당하는 ContextPath 변수 주입 */
	@Value("#{servletContext.contextPath}")
	String contextPath;

	/** 책 대여중 */
	@RequestMapping(value = "/rental", method = RequestMethod.POST)
	public Map<String, Object> rental_book(@RequestBody Rental rental,
			@RequestHeader("Token") String token) {
		
		if (token == null) {
			return webHelper.getJsonWarning("토큰이 없습니다.");
		}

		// jwt 유효성 검사
		Map<String, Object> claimMap = null;
		try {
			claimMap = jwtService.verifyJWT(token);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (claimMap == null) {
			return webHelper.getJsonWarning("토큰이 유효하지 않습니다.");
		}


		// 저장할 값들을 Beans에 담는다.
		Rental input = new Rental();
		input.setBook_id(rental.getBook_id());
		input.setOwner_id(rental.getOwner_id());
		input.setRenter_id(rental.getRenter_id());
		input.setReturndate(rental.getReturndate());

		// 저장된 결과를 조회하기 위한 객체
		//List<Rental> output = null;

		try {
				// 데이터 저장
				rentalService.addItem(input);

				// 데이터 조회
				//output = rentalService.getOwnerList(input);
		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		/** 3) 결과를 확인하기 위한 페이지 연동 */
		// 저장 결과를 확인하기 위해 데이터 저장 시 생성된 PK값을 상세 페이지로 전달해야 한다.
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("item", output);
		//map.putAll(claimMap);
		return webHelper.getJsonData(map);
	}
	
	/** 책 대여 상태 수정 */
	@RequestMapping(value = "/rental", method = RequestMethod.PUT)
	public Map<String, Object> rental_update(@RequestBody Rental rental,
			@RequestHeader("Token") String token) {
		
		if (token == null) {
			return webHelper.getJsonWarning("토큰이 없습니다.");
		}

		// jwt 유효성 검사
		Map<String, Object> claimMap = null;
		try {
			claimMap = jwtService.verifyJWT(token);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (claimMap == null) {
			return webHelper.getJsonWarning("토큰이 유효하지 않습니다.");
		}


		// 저장할 값들을 Beans에 담는다. -  Param으로 Rental Id값, 반납상태, 반납일정 필요
		Rental input = new Rental();
		input.setId(rental.getId());
		input.setIsrent(rental.getIsrent());
		input.setReturndate(rental.getReturndate());

		// 저장된 결과를 조회하기 위한 객체
		//Rental output = null;

		try {
				// 데이터 저장
				rentalService.updateState(input);

				// 데이터 조회
				//output = rentalService.getRentalItem(input);
		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}

		/** 3) 결과를 확인하기 위한 페이지 연동 */
		// 저장 결과를 확인하기 위해 데이터 저장 시 생성된 PK값을 상세 페이지로 전달해야 한다.
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("item", output);
		//map.putAll(claimMap);
		return webHelper.getJsonData(map);
	}
	
	/** 대여 목록 조회 - Owner 빌려준 책 목록 조회 */
	@RequestMapping(value = "/owner/{user_id}", method = RequestMethod.GET)
	public Map<String, Object> rental(@PathVariable("user_id") int user_id,
			@RequestHeader("Token") String token) {
		
		if (token == null) {
			return webHelper.getJsonWarning("토큰이 없습니다.");
		}

		// jwt 유효성 검사
		Map<String, Object> claimMap = null;
		try {
			claimMap = jwtService.verifyJWT(token);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (claimMap == null) {
			return webHelper.getJsonWarning("토큰이 유효하지 않습니다.");
		}


		// 조회할 값들을 Beans에 담는다.
		Rental input = new Rental();
		input.setOwner_id(user_id);

		// 저장된 결과를 조회하기 위한 객체
		List<Rental> output_owner = null;
		
		//유효한 사용자 번호인지 확인
		Users user = new Users();
		user.setId(user_id);
		Users user_output = new Users();
		
		List<BookList> output = new ArrayList<BookList>();

		try {
				// 데이터 조회
			user_output = usersService.getUserItem(user);
			output_owner = rentalService.getOwnerList(input);
			
			
		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}
		
		if(user_output == null) {
			System.out.println("사용자 번호가 유효하지 않음");
		}
		
		//저장된 결과를 조회할 최종 객체
		for (int i = 0; i < output_owner.size(); i++) {
			Rental item = output_owner.get(i);
			BookList booklist = new BookList();
			booklist.setTitle(item.getTitle());
			booklist.setImg(item.getImg());
			booklist.setCreated_at(item.getCreated_at());
			output.add(booklist);
		}
		
		

		/** 3) 결과를 확인하기 위한 페이지 연동 */
		// 저장 결과를 확인하기 위해 데이터 저장 시 생성된 PK값을 상세 페이지로 전달해야 한다.
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("output", output);
		return webHelper.getJsonData(map);
	}
	
	/** 대여 목록 조회 - Renter 빌린 책 목록 조회 */
	@RequestMapping(value = "/renter/{user_id}", method = RequestMethod.GET)
	public Map<String, Object> rental2(@PathVariable("user_id") int user_id,
			@RequestHeader("Token") String token) {
		
		if (token == null) {
			return webHelper.getJsonWarning("토큰이 없습니다.");
		}

		// jwt 유효성 검사
		Map<String, Object> claimMap = null;
		try {
			claimMap = jwtService.verifyJWT(token);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (claimMap == null) {
			return webHelper.getJsonWarning("토큰이 유효하지 않습니다.");
		}


		// 조회할 값들을 Beans에 담는다.
		Rental input = new Rental();
		input.setRenter_id(user_id);

		// 저장된 결과를 조회하기 위한 객체
		List<Rental> output_renter = null;
		
		//유효한 사용자 번호인지 확인
		Users user = new Users();
		user.setId(user_id);
		Users user_output = new Users();
		
		List<BookList> output = new ArrayList<BookList>();

		try {
				// 데이터 조회
			user_output = usersService.getUserItem(user);
			output_renter = rentalService.getRenterList(input);
			
			
		} catch (Exception e) {
			return webHelper.getJsonError(e.getLocalizedMessage());
		}
		
		if(user_output == null) {
			System.out.println("사용자 번호가 유효하지 않음");
		}
		
		//저장된 결과를 조회할 최종 객체
		for (int i = 0; i < output_renter.size(); i++) {
			Rental item = output_renter.get(i);
			BookList booklist = new BookList();
			booklist.setTitle(item.getTitle());
			booklist.setImg(item.getImg());
			booklist.setCreated_at(item.getCreated_at());
			output.add(booklist);
		}
		
		

		/** 3) 결과를 확인하기 위한 페이지 연동 */
		// 저장 결과를 확인하기 위해 데이터 저장 시 생성된 PK값을 상세 페이지로 전달해야 한다.
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("output", output);
		return webHelper.getJsonData(map);
	}
	
	
}
