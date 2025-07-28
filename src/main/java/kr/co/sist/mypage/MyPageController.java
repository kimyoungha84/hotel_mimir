package kr.co.sist.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable; // PathVariable 추가
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.member.CustomUserDetails;
import kr.co.sist.member.MemberDTO;
import kr.co.sist.member.MemberService;
import kr.co.sist.member.RoomReservationDTO;

import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MemberService memberService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

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
    public String confirmPasswordForm(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(required = false) String purpose, Model model) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }
        model.addAttribute("purpose", purpose);
        return "mypage/confirmPassword";
    }

    @PostMapping("/confirm-password")
    public String confirmPassword(@RequestParam String password,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  HttpSession session,
                                  RedirectAttributes redirectAttrs,
                                  @RequestParam(required = false) String purpose) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }

        String email = userDetails.getUsername();
        if (memberService.checkPassword(email, password)) {
            session.setAttribute("passwordConfirmed", true);
            if ("resetPassword".equals(purpose)) {
                return "redirect:/mypage/edit-password";
            } else if ("withdraw".equals(purpose)) {
                return "redirect:/mypage/withdraw";
            } else {
                return "redirect:/mypage/edit-profile";
            }
        } else {
            redirectAttrs.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/confirm-password" + (purpose != null ? "?purpose=" + purpose : "");
        }
    }

    @GetMapping("/edit-profile")
    public String editProfileForm(HttpSession session, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Boolean passwordConfirmed = (Boolean) session.getAttribute("passwordConfirmed");
        if (passwordConfirmed == null || !passwordConfirmed) {
            return "redirect:/mypage/confirm-password?purpose=editProfile";
        }
        session.removeAttribute("passwordConfirmed");

        String email = userDetails.getUsername();
        MemberDTO memberInfo = memberService.getMemberByEmail(email);
        model.addAttribute("memberInfo", memberInfo);

        return "mypage/editProfile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute MemberDTO memberDTO, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttrs) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }

        memberDTO.setEmail_id(userDetails.getUsername());
        boolean success = memberService.updateProfile(memberDTO);

        if (success) {
            redirectAttrs.addFlashAttribute("success", "회원정보가 성공적으로 수정되었습니다.");
        } else {
            redirectAttrs.addFlashAttribute("error", "회원정보 수정에 실패했습니다.");
        }

        return "redirect:/mypage";
    }

    @GetMapping("/edit-password")
    public String editPasswordForm() {
        return "mypage/editPassword";
    }

    @PostMapping("/update-password")
    @ResponseBody
    public Map<String, Boolean> updatePassword(@RequestBody Map<String, String> body, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String newPassword = body.get("newPassword");
        String email = userDetails.getUsername();

        boolean result = memberService.resetPassword(email, newPassword);
        return Map.of("success", result);
    }

    @GetMapping("/expected-room-resv-count")
    @ResponseBody
    public int getExpectedRoomResvCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return 0;
        }
        return memberService.getExpectedRoomResvCount(String.valueOf(userDetails.getUserNum()));
    }
    
    @GetMapping("/expected-dining-resv-count")
    @ResponseBody
    public int getExpectedDiningResvCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return 0;
        }
        return memberService.getExpectedDiningResvCount(String.valueOf(userDetails.getUserNum()));
    }

    @GetMapping("/withdraw")
    public String withdrawForm(HttpSession session, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }

        Boolean passwordConfirmed = (Boolean) session.getAttribute("passwordConfirmed");
        if (passwordConfirmed == null || !passwordConfirmed) {
            return "redirect:/mypage/confirm-password?purpose=withdraw";
        }
        session.removeAttribute("passwordConfirmed");

        return "mypage/withdraw";
    }

    @PostMapping("/withdraw-member")
    @ResponseBody
    public Map<String, Boolean> withdrawMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               HttpServletRequest request,
                                               HttpServletResponse response,
                                               HttpSession session) {
        Map<String, Boolean> responseMap = new HashMap<>();

        if (userDetails == null) {
            responseMap.put("success", false);
            return responseMap;
        }

        String email = userDetails.getUsername();
        boolean success = memberService.withdrawMember(email);

        if (success) {
            SecurityContextHolder.clearContext();
            session.invalidate();

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        responseMap.put("success", success);
        return responseMap;
    }
    //객실 예약내역
    @GetMapping("/room-reservations")
    public String roomReservations(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }
        String userNum = String.valueOf(userDetails.getUserNum());
        List<RoomReservationDTO> myRoomReservationList = memberService.getRoomReservationsByUserNum(userNum);
        model.addAttribute("myRoomReservationList", myRoomReservationList);
        return "mypage/roomResvList";
    }

    @GetMapping("/room-reservations/{reservationId}")
    public String roomReservationDetail(@PathVariable int reservationId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }

        RoomReservationDTO roomReservationDetail = memberService.getRoomReservationDetail(reservationId);

         if (roomReservationDetail.getUser_num() != userDetails.getUserNum()) {
             return "redirect:/mypage/room-reservations"; 
         }

        model.addAttribute("roomReservationDetail", roomReservationDetail);
        return "mypage/roomResvDetail";
    }

    @PostMapping("/cancel-room-reservation")
    @ResponseBody
    public Map<String, Boolean> cancelRoomReservation(
            @RequestBody Map<String, Integer> body,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Boolean> response = new HashMap<>();

        if (userDetails == null) {
            response.put("success", false);
            return response;
        }

        int reservationId = body.get("reservationId");

        RoomReservationDTO reservation = memberService.getRoomReservationDetail(reservationId);

        if (reservation == null || reservation.getUser_num() != userDetails.getUserNum()) {
            response.put("success", false);
            return response;
        }

        boolean success = memberService.cancelRoomReservation(reservationId);
        response.put("success", success);
        return response;
    }
    
  //다이닝 예약내역
    @GetMapping("/dining-reservations")
    public String diningReservations(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }
        String userNum = String.valueOf(userDetails.getUserNum());
        List<DiningResvDTO> myDiningReservationList = memberService.getDiningReservationsByUserNum(userNum);
        model.addAttribute("myDiningReservationList", myDiningReservationList);
        return "mypage/diningResvList";
    }

    @GetMapping("/dining-reservations/{reservationId}")
    public String diningReservationDetail(@PathVariable int reservationId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/member/loginFrm";
        }

        DiningResvDTO diningReservationDetail = memberService.getDiningReservationDetail(reservationId);

         if (diningReservationDetail.getUserNum() != userDetails.getUserNum()) {
             return "redirect:/mypage/dining-reservations"; 
         }

        model.addAttribute("diningReservationDetail", diningReservationDetail);
        return "mypage/diningResvDetail";
    }

    @PostMapping("/cancel-dining-reservation")
    @ResponseBody
    public Map<String, Boolean> cancelDiningReservation(
            @RequestBody Map<String, Integer> body,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Boolean> response = new HashMap<>();

        if (userDetails == null) {
            response.put("success", false);
            return response;
        }

        int reservationId = body.get("reservationId");

        DiningResvDTO reservation = memberService.getDiningReservationDetail(reservationId);

        if (reservation == null || reservation.getUserNum() != userDetails.getUserNum()) {
            response.put("success", false);
            return response;
        }

        boolean success = memberService.cancelDiningReservation(reservationId);
        response.put("success", success);
        return response;
    }
}