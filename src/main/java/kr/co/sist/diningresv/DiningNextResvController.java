package kr.co.sist.diningresv;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.dining.user.DiningDomain;
import kr.co.sist.member.CustomUserDetails;
import kr.co.sist.nonmember.NonMemberService;
import kr.co.sist.payment.PaymentService;

@Controller
public class DiningNextResvController {
	
    @Autowired
    private DiningResvService drs;
    
    @Autowired
    private NonMemberService nms;
    
    @Autowired
    private PaymentService ps;
	
	@GetMapping("/diningNextResv")
	public String nextReservation(@RequestParam("dining") int diningId,
	                              @RequestParam int adult,
	                              @RequestParam int child,
	                              @RequestParam String date,
	                              @RequestParam String time,
	                              @RequestParam String meal,
	                              Model model) {
		
        String formattedDate;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = inputFormat.parse(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd(E)", Locale .KOREAN);
            formattedDate = outputFormat.format(parsedDate);
        } catch (Exception e) {
            formattedDate = date;
        }
		
        String mealLabel = switch (meal) {
            case "Lunch" -> "Lunch (11:30~14:30)";
            case "Dinner" -> "Dinner (17:30~22:00)";
            default -> meal;
        };
        
        DiningDomain diningInfo = drs.searchDining(diningId);
		
        model.addAttribute("diningId", diningId);
        
	    model.addAttribute("dining", diningInfo.getDining_name());
	    model.addAttribute("adult", adult);
	    model.addAttribute("child", child);
	    model.addAttribute("date", date);
	    model.addAttribute("time", time);
	    model.addAttribute("meal", meal);
	    
        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("mealLabel", mealLabel);
	    
	    return "dining_resv/dining_next_resv/diningNextResv";
	}
	
	@PostMapping("/diningNextResv")
	public String insertReservation(
	    @RequestParam String reservationName,
	    @RequestParam String reservationEmail,
	    @RequestParam String reservationTell,
	    @RequestParam(required = false) String reservationRequest,
	    @RequestParam String paymentType,
	    @RequestParam int adult,
	    @RequestParam int child,
	    @RequestParam String date,
	    @RequestParam String time,
	    @RequestParam String meal,
	    @RequestParam int diningId,
	    @AuthenticationPrincipal CustomUserDetails loginUser,
	    Model model) {

	    DiningResvDTO dto = new DiningResvDTO();
	    int totalCount = adult + child;

	    dto.setReservationName(reservationName);
	    dto.setReservationEmail(reservationEmail);
	    dto.setReservationTell(reservationTell);
	    dto.setReservationRequest(reservationRequest);
	    dto.setPaymentType(paymentType);
	    dto.setReservationCount(totalCount);
	    dto.setDiningId(diningId);

	    // 날짜/시간 세팅
	    try {
	        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	        dto.setReservationDate(new java.sql.Date(sdfDate.parse(date).getTime()));

	        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
	        dto.setReservationTime(new java.sql.Time(sdfTime.parse(time).getTime()));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // ✅ 회원 / 비회원 분기
	    if (loginUser != null) {
	        // 🟢 회원일 경우
	        dto.setUserNum(loginUser.getUserNum());
	        dto.setReservationType("회원");
	    } else {
	        // 🔵 비회원일 경우
	        int nonMemId = nms.searchNonMemberSeq();
	        dto.setNonMemId(nonMemId);
	        nms.insertNonMember2(dto);
	        dto.setReservationType("비회원");
	    }

	    // 결제
	    int paymentId = ps.searchPaymentSeq();
	    dto.setPaymentId(paymentId);
	    dto.setPaymentStatus("결제완료");
	    ps.insertPayment2(dto);

	    // 예약
	    int reservationId = drs.searchResvSeq();
	    dto.setReservationId(reservationId);
	    drs.insertDiningResv(dto);

	    // View로 값 전달
	    String formattedDate;
	    try {
	        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd(E)", Locale.KOREAN);
	        formattedDate = outputFormat.format(dto.getReservationDate());
	    } catch (Exception e) {
	        formattedDate = date;
	    }

	    String mealLabel = switch (meal) {
	        case "Lunch" -> "Lunch (11:30~14:30)";
	        case "Dinner" -> "Dinner (17:30~22:00)";
	        default -> meal;
	    };

	    model.addAttribute("reservationId", reservationId);
	    model.addAttribute("reservationName", reservationName);
	    model.addAttribute("reservationTell", reservationTell);
	    model.addAttribute("dining", drs.searchDining(diningId).getDining_name());
	    model.addAttribute("reservationCount", totalCount);
	    model.addAttribute("reservationDate", formattedDate);
	    model.addAttribute("reservationTime", time);
	    model.addAttribute("mealLabel", mealLabel);

	    return "dining_resv/dining_next_resv/diningResvComplete";
	}
	
}
