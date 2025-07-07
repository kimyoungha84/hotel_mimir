package kr.co.sist.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.member.dto.MemberDTO;

@Controller
public class MemberController {
	
	/*
	 * ============ 회원가입 ============
	 */
	
	//회원가입 폼 호출
	@GetMapping("/member/registerFrm")
	public String registerFrm(Model model) {
		model.addAttribute("memberDTO", new MemberDTO());
		
		return "member/registerFrm";
	}
	
	//이메일 인증 팝업 호출
	@GetMapping("/member/email-popup")
	public String emailPopup() {
		
		return "member/email_popup";
	}
	
	
	
	
	/*
	 * ============ 회원로그인 ============
	 */
}
