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
	
	// 이메일 인증번호 발송
    @PostMapping("/send-auth-code")
    public Map<String, String> sendAuthCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return memberService.sendAuthCodeWithJwt(email);
    }

    // 이메일 인증번호 검증
    @PostMapping("/verify-auth-code")
    public String verifyAuthCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        String token = body.get("token");

        boolean result = memberService.verifyAuthCodeWithJwt(token, email, code);
        return result ? "success" : "fail";
    }

	
	
	
	
	
	/*
	 * ============ 회원로그인 ============
	 */
}
