package kr.co.sist.member;

import java.util.Map;

public interface MemberService {
	
	LoginResponseDTO login(LoginRequestDTO loginDTO);

	boolean registerMember(MemberDTO mDTO);
	
	void updateRefreshToken(String emailId, String refreshToken);
	
    Map<String, String> sendAuthCodeWithJwt(String email);
    
    boolean verifyAuthCodeWithJwt(String token, String email, String code);

    void invalidateRefreshToken(String emailId); // 로그아웃 시 Refresh Token 무효화

    void updateLastLoginTime(String emailId);

    boolean isEmailDuplicated(String email);

    MemberDTO getMemberByEmail(String email);
    
}
