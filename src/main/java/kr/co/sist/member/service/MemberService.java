package kr.co.sist.member.service;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.member.dto.MemberDTO;

public interface MemberService {
	
	boolean registerMember(MemberDTO mDTO);
	
    Map<String, String> sendAuthCodeWithJwt(String email);
    
    boolean verifyAuthCodeWithJwt(String token, String email, String code);
}
