package study.spring.hellobook.service;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.stereotype.Service;
import study.spring.hellobook.model.Users;

@Service
public interface JwtService {
	public String createToken(String email) throws Exception;
	
	 public Map<String, Object> verifyJWT(String jwt) throws UnsupportedEncodingException;

}
