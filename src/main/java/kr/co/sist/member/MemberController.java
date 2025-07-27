package kr.co.sist.member;

import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.member.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    							BindingResult result, RedirectAttributes redirectAttrs) {
    	
    	//유효성 검사 실패 시 폼 재호출
    	if(result.hasErrors()) {
    		return "member/registerFrm";
    	}
    	
    	//이메일 인증 토큰 검증
    	boolean isVerified = memberService.verifyAuthCodeWithJwt(
    				token, mDTO.getEmail_id(), mDTO.getEmail_auth());
    	
    	if(!isVerified) {
    		redirectAttrs.addFlashAttribute("error", "이메일 인증이 유효하지 않습니다. 다시 시도해주세요.");
    		
    		return "redirect:/member/registerFrm";
    	}
    	
    	
    	boolean success = memberService.registerMember(mDTO);
    	
    	if(success) {
    		return "redirect:/";
    	} else {
    		redirectAttrs.addFlashAttribute("error", "회원가입 처리 중 오류가 발생했습니다.");
    		
    		return "redirect:/member/registerFrm";
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
            LoginResponseDTO loginResponseDTO = memberService.login(loginRequestDTO);

            String accessToken = loginResponseDTO.getAccessToken();
            String refreshToken = loginResponseDTO.getRefreshToken();

            memberService.updateRefreshToken(email_id, refreshToken);

            Cookie jwtCookie = new Cookie("access_token", accessToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(-1); //종료시 삭제
            response.addCookie(jwtCookie);

            return "redirect:/";

        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("loginError", e.getMessage());
            return "redirect:/member/loginFrm";
        }
    }
    
    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 1. 클라이언트 측 Access Token 쿠키 삭제
        Cookie jwtCookie = new Cookie("access_token", null); // 쿠키 값 null로 설정
        jwtCookie.setMaxAge(0); // 즉시 만료
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        
        // 2. Spring Security 컨텍스트 클리어
        SecurityContextHolder.clearContext();
        
        // 3. 서버 측 Refresh Token 무효화 (DB에서 삭제 또는 NULL로 업데이트)
        if (userDetails != null) {
            memberService.invalidateRefreshToken(userDetails.getUsername()); // email_id로 Refresh Token 무효화
        }
        

        return "redirect:/"; // 로그아웃 후 메인 페이지로 리다이렉트
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
          
          if(memberService.isEmailDuplicated(email)) {
          	return Map.of("error", "이미 가입된 이메일입니다.");
          }
          
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
      
    /*
	 * ============ 아이디 찾기 ============
	 */
	
	@GetMapping("/find-id")
	public String findIdForm() {
	    return "member/findId";
	}
	
	@PostMapping("/send-auth-code-find-id")
	@ResponseBody
	public Map<String, String> sendAuthCodeFindId(@RequestBody Map<String, String> body) {
	    String name = body.get("name");
	    String email = body.get("email");
	
	    if (!memberService.isUserExist(name, email)) {
	        return Map.of("error", "일치하는 사용자 정보가 없습니다.");
	    }
	
	    return memberService.sendAuthCodeWithJwt(email);
	}
	
	@PostMapping("/verify-auth-code-find-id")
	@ResponseBody
	public Map<String, Boolean> verifyAuthCodeFindId(@RequestBody Map<String, String> body) {
	    String email = body.get("email");
	    String code = body.get("code");
	    String token = body.get("token");
	
	    boolean result = memberService.verifyAuthCodeWithJwt(token, email, code);
	    return Map.of("success", result);
	}
	
	@GetMapping("/find-id-result")
	public String findIdResult(@RequestParam String email, Model model) {
	    MemberDTO memberInfo = memberService.getMemberByEmail(email);
	    if (memberInfo != null) {
	        model.addAttribute("memberInfo", memberInfo);
	    } else {
	        model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
	    }
	    return "member/findIdResult";
	}
	
	/*
	 * ============ 비밀번호 찾기 ============
	 */
	
	@GetMapping("/find-password")
	public String findPasswordForm() {
	    return "member/find-password";
	}
	
	@PostMapping("/send-auth-code-find-password")
	@ResponseBody
	public Map<String, String> sendAuthCodeFindPassword(@RequestBody Map<String, String> body) {
	    String userId = body.get("userId");
	    String name = body.get("name");
	    String email = body.get("email");
	
	    if (!memberService.isUserExistByIdAndNameAndEmail(userId, name, email)) {
	        return Map.of("error", "일치하는 사용자 정보가 없습니다.");
	    }
	
	    return memberService.sendAuthCodeWithJwt(email);
	}
	
	@PostMapping("/verify-auth-code-find-password")
	@ResponseBody
	public Map<String, Boolean> verifyAuthCodeFindPassword(@RequestBody Map<String, String> body) {
	    String email = body.get("email");
	    String code = body.get("code");
	    String token = body.get("token");
	
	    boolean result = memberService.verifyAuthCodeWithJwt(token, email, code);
	    return Map.of("success", result);
	}
	
	@GetMapping("/reset-password")
	public String resetPasswordForm() {
	    return "member/reset-password";
	}
	
	@PostMapping("/reset-password")
	@ResponseBody
	public Map<String, Boolean> resetPassword(@RequestBody Map<String, String> body) {
	    String email = body.get("email");
	    String newPassword = body.get("newPassword");
	
	    boolean result = memberService.resetPassword(email, newPassword);
	    return Map.of("success", result);
	}

	
}
