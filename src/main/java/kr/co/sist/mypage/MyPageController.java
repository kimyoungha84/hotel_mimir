package kr.co.sist.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.sist.member.CustomUserDetails;
import kr.co.sist.member.MemberDTO;
import kr.co.sist.member.MemberService;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String myPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }
        
        String email = userDetails.getUsername();
        MemberDTO memberInfo = memberService.getMemberByEmail(email);
        
        model.addAttribute("memberInfo", memberInfo);
        
        return "mypage/myPage";
    }

    @GetMapping("/confirm-password")
    public String confirmPasswordForm() {
        return "mypage/confirmPassword";
    }

    @GetMapping("/edit-profile")
    public String editProfileForm() {
        return "mypage/editProfile";
    }

    @PostMapping("/confirm-password")
    public String confirmPassword(@RequestParam String password,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttrs) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }

        String email = userDetails.getUsername();
        if (memberService.checkPassword(email, password)) {
            return "redirect:/mypage/edit-profile"; // 비밀번호 일치 시 수정 페이지로 리다이렉트
        } else {
            redirectAttrs.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/confirm-password"; // 불일치 시 에러 메시지와 함께 다시 확인 페이지로
        }
    }
}
