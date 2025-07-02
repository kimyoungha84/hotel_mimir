package kr.co.sist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
	
	/*
	 * ============ 회원가입 ============
	 */
	
	//회원가입 폼 호출
	@GetMapping("/member/registerFrm")
	public String registerFrm() {
		
		return "member/registerFrm";
	}
	
	
	
	
	/*
	 * ============ 회원로그인 ============
	 */
}
