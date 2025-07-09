package kr.co.sist.member.service;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.member.dto.MemberDTO;

public interface MemberService {
	void sendAuthCode(String email, HttpSession session);
    boolean verifyAuthCode(String email, String code, HttpSession session);
    void registerMember(MemberDTO mDTO);
}
