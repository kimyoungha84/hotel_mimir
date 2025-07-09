package kr.co.sist.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.member.dto.MemberDTO;
import kr.co.sist.member.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	/*
	 * ============ 회원가입 ============
	 */
	
	//회원가입 폼 호출
	@GetMapping("/registerFrm")
	public String registerFrm(Model model) {
		model.addAttribute("memberDTO", new MemberDTO());
		
		return "member/registerFrm";
	}
	
	//이메일 인증 팝업 호출
	@GetMapping("/email-popup")
	public String emailPopup() {
		
		return "member/email_popup";
	}
	
	 @PostMapping("/send-auth-code")
	    @ResponseBody
	    public String sendAuthCode(@RequestBody Map<String, String> body, HttpSession session) {
	        String email = body.get("email");
	        memberService.sendAuthCode(email, session);
	        return "인증번호가 발송되었습니다.";
	    }

	    // 인증번호 검증
	    @PostMapping("/verify-auth-code")
	    @ResponseBody
	    public String verifyAuthCode(@RequestBody Map<String, String> body, HttpSession session) {
	        String email = body.get("email");
	        String code = body.get("code");
	        boolean verified = memberService.verifyAuthCode(email, code, session);
	        return verified ? "success" : "fail";
	    }

	    // 회원가입 처리
	    @PostMapping("/register")
	    public String register(@ModelAttribute MemberDTO dto, Model model) {
	        memberService.registerMember(dto);
	        model.addAttribute("name", dto.getUser_name());
	        return "member/register_result"; // 회원가입 완료 페이지 (필요 시 생성)
	    }
	
	
	
	
	/*
	 * ============ 회원로그인 ============
	 */
}
