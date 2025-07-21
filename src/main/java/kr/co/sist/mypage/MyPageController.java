package kr.co.sist.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
