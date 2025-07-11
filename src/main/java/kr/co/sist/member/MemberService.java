package kr.co.sist.member;

import java.util.Map;

public interface MemberService {
	
	LoginResponseDTO login(LoginRequestDTO loginDTO);
	
	boolean registerMember(MemberDTO mDTO);
	
    Map<String, String> sendAuthCodeWithJwt(String email);
    
    boolean verifyAuthCodeWithJwt(String token, String email, String code);
}
