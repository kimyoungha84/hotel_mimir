package kr.co.sist.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.sist.util.JwtUtil;
import kr.co.sist.util.LoginJwtUtil;

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

	//회원가입처리
    @PostMapping("/register")
    public String registerMember(@ModelAttribute @Valid MemberDTO mDTO,
    							@RequestParam("token") String token,
    							BindingResult result, Model model) {
    	
    	//유효성 검사 실패 시 폼 재호출
    	if(result.hasErrors()) {
    		return "member/registerFrm";
    	}
    	
    	//이메일 인증 토큰 검증
    	boolean isVerified = memberService.verifyAuthCodeWithJwt(
    				token, mDTO.getEmail_id(), mDTO.getEmail_auth());
    	
    	if(!isVerified) {
    		model.addAttribute("emailError", "이메일 인증이 완료되지 않았습니다.");
    		
    		return "member/registerFrm";
    	}
    	
    	
    	boolean success = memberService.registerMember(mDTO);
    	
    	if(success) {
    		return "redirect:/";
    	} else {
    		model.addAttribute("errMsg", "회원가입 처리 중 오류가 발생했습니다.");
    		
    		return "member/registerFrm";
    	}
    	
    }
	
	
	/*
	 * ============ 회원로그인 ============
	 */
    
    //로그인 폼 호출
    @GetMapping("/loginFrm")
    public String loginFrm() {
        return "member/loginFrm";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email_id,
                        @RequestParam String password,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttrs) {

        try {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email_id, password);
            loginRequestDTO.setEmail_id(email_id);
            loginRequestDTO.setPassword(password);

            LoginResponseDTO loginResponseDTO = memberService.login(loginRequestDTO);

            String accessToken = loginResponseDTO.getAccessToken();
            String refreshToken = loginResponseDTO.getRefreshToken();

            memberService.updateRefreshToken(email_id, refreshToken);

            Cookie jwtCookie = new Cookie("access_token", accessToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 30); // 30분
            response.addCookie(jwtCookie);

            return "redirect:/";

        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("loginError", e.getMessage());
            return "redirect:/member/loginFrm";
        }
    }
    
    
    /*
	 * ============ 이메일 인증 ============
	 */
  //이메일 인증 팝업 호출
  	@GetMapping("/email-popup")
  	public String emailPopup() {
  		
  		return "member/email_popup";
  	}
  	
  	// 이메일 인증번호 발송
      @PostMapping("/send-auth-code")
      @ResponseBody
      public Map<String, String> sendAuthCode(@RequestBody Map<String, String> body) {
          String email = body.get("email");
          return memberService.sendAuthCodeWithJwt(email);
      }

      // 이메일 인증번호 검증
      @PostMapping("/verify-auth-code")
      @ResponseBody
      public String verifyAuthCode(@RequestBody Map<String, String> body) {
          String email = body.get("email");
          String code = body.get("code");
          String token = body.get("token");

          boolean result = memberService.verifyAuthCodeWithJwt(token, email, code);
          return result ? "success" : "fail";
      }
}
