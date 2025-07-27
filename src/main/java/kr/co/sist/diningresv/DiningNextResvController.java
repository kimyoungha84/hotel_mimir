package kr.co.sist.diningresv;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
import kr.co.sist.administrator.Util.AdministratorSendMail;
import kr.co.sist.dining.user.DiningDomain;
import kr.co.sist.dining.user.DiningService;
import kr.co.sist.dining.user.RepMenuDomain;
import kr.co.sist.diningslot.DiningTimeSlotService;
import kr.co.sist.member.CustomUserDetails;
import kr.co.sist.nonmember.NonMemberService;
import kr.co.sist.payment.PaymentService;

@Controller
public class DiningNextResvController {
	
    @Autowired
    private DiningResvService drs;
    
    @Autowired
    private DiningTimeSlotService dtss;
    
	@Autowired
	private DiningService ds;
    
    @Autowired
    private NonMemberService nms;
    
    @Autowired
    private PaymentService ps;
    
    @Autowired
    private AdministratorSendMail sendMail;
	
	@GetMapping("/diningNextResv")
	public String nextReservation(
								  @RequestParam("diningId") int diningId,
	                              @RequestParam int adult,
	                              @RequestParam int child,
	                              @RequestParam String date,
	                              @RequestParam String time,
	                              @RequestParam String meal,
	                              @RequestParam(required = false, defaultValue = "0") int lunchQty,
	                              @RequestParam(required = false, defaultValue = "0") int dinnerQty,
	                              @AuthenticationPrincipal CustomUserDetails loginUser,
	                              Model model) {
		
		List<RepMenuDomain> menuList = ds.searchRepMenu(diningId);
		
	    int totalPrice = 0;
	    
	    if (meal.equals("Lunch")) {
	        Optional<RepMenuDomain> lunchMenu = menuList.stream()
	            .filter(menu -> "중식".equals(menu.getDescription()))
	            .findFirst();

	        if (lunchMenu.isPresent()) {
	            totalPrice = lunchMenu.get().getPrice() * lunchQty;
	            model.addAttribute("menuName", lunchMenu.get().getMenu_name());
	        }

	    } else if (meal.equals("Dinner")) {
	        Optional<RepMenuDomain> dinnerMenu = menuList.stream()
	            .filter(menu -> "석식".equals(menu.getDescription()))
	            .findFirst();

	        if (dinnerMenu.isPresent()) {
	            totalPrice = dinnerMenu.get().getPrice() * dinnerQty;
	            model.addAttribute("menuName", dinnerMenu.get().getMenu_name());
	        }
	    }
		
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
	    model.addAttribute("diningName", diningInfo.getDining_name());
	    model.addAttribute("adult", adult);
	    model.addAttribute("child", child);
	    model.addAttribute("date", date);
	    model.addAttribute("time", time);
	    model.addAttribute("meal", meal);
	    
	    model.addAttribute("totalPrice", totalPrice);
	    model.addAttribute("lunchQty", lunchQty);
	    model.addAttribute("dinnerQty", dinnerQty);
	    
        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("mealLabel", mealLabel);
	    
        if (loginUser != null) {
        	DiningResvDTO user = drs.loginUserData(loginUser.getUserNum());
        	model.addAttribute("userName", user.getUserName());
        	model.addAttribute("userEmail", user.getUserEmail());
            model.addAttribute("userTel", user.getUserTel());
        }
        
	    return "dining_resv/dining_next_resv/diningNextResv";
	    
	}
	
	@PostMapping("/diningNextResv")
	public String insertReservation(
								@RequestParam String reservationName,
								@RequestParam String reservationEmail,
								@RequestParam String reservationTell,
								@RequestParam String reservationRequest,
								@RequestParam String paymentType,
								@RequestParam int paymentPrice,
								@RequestParam int adult,
								@RequestParam int child,
								@RequestParam String date,
								@RequestParam String time,
								@RequestParam String meal,
								@RequestParam int diningId,
							    @RequestParam(required = false, defaultValue = "0") int lunchQty,
							    @RequestParam(required = false, defaultValue = "0") int dinnerQty,
								@AuthenticationPrincipal CustomUserDetails loginUser,
								Model model) {

	    DiningResvDTO dto = new DiningResvDTO();
	    
	    int totalCount = adult + child;
	    
	    if (reservationRequest == null || reservationRequest.trim().isEmpty()) {
	    	
	        reservationRequest = "없음";
	        
	    }

	    dto.setReservationName(reservationName);
	    dto.setReservationEmail(reservationEmail);
	    dto.setReservationTell(reservationTell);
	    dto.setReservationRequest(reservationRequest);
	    dto.setPaymentType(paymentType);
	    dto.setReservationCount(totalCount);
	    dto.setDiningId(diningId);

	    java.sql.Timestamp reservationTime = null;
	    java.sql.Date reservationDate = null;
	    
	    // 날짜 / 시간 세팅
	    try {
	    	
	        String dateTimeStr = date + " " + time;
	        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Date parsedDateTime = sdfDateTime.parse(dateTimeStr);
	        
	        reservationTime = new Timestamp(parsedDateTime.getTime());
	        reservationDate = new java.sql.Date(parsedDateTime.getTime());
	        
	        dto.setReservationTime(reservationTime);
	        dto.setReservationDate(reservationDate);

	    } catch (Exception e) {
	    	
	        e.printStackTrace();
	        
	    }

	    // 회원 / 비회원 분기
	    if (loginUser != null) {
	        // 회원일 경우
	        dto.setUserNum(loginUser.getUserNum());
	        dto.setNonMemId(null);
	        
	        dto.setReservationType("회원");

	    } else {
	        // 비회원일 경우
	    	int nonMemId = nms.searchNonMemberSeq();
	    	
	    	dto.setUserNum(null);
	        dto.setNonMemId(nonMemId);
	        dto.setNonMemName(reservationName);
	        dto.setNonMemTel(reservationTell);
	        dto.setNonMemEmail(reservationEmail);
	        
	        nms.insertNonMember2(dto);
	        
	        dto.setReservationType("비회원");
	        
	    }

	    // 결제
	    int paymentId = ps.searchPaymentSeq();
	    
	    dto.setPaymentId(paymentId);
	    dto.setPaymentPrice(paymentPrice);
	    
	    if ("현장결제".equals(paymentType)) {
	    	
	        dto.setPaymentStatus("대기");
	        
	        dto.setPaymentTime(null);
	        
	    } else {
	    	
	        dto.setPaymentStatus("완료");
	        
	        dto.setPaymentTime(new Timestamp(System.currentTimeMillis()));
	        
	    }
	    
	    ps.insertPayment2(dto);

	    // 예약
	    int reservationId = drs.searchResvSeq();
	    
	    dto.setReservationId(reservationId);
	    dto.setReservationStatus("진행");

	    drs.insertDiningResv(dto);
	    
	    dtss.handleSlot(diningId, reservationDate, reservationTime, totalCount);
	    
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

	    String email = reservationEmail;

	    Map<String, Object> variables = new HashMap<>();
	    variables.put("reservationId", reservationId);
	    variables.put("reservationName", reservationName);
	    variables.put("reservationTell", reservationTell);
	    variables.put("dining", drs.searchDining(diningId).getDining_name());
	    variables.put("reservationCount", totalCount);
	    variables.put("reservationDate", formattedDate);
	    variables.put("reservationTime", time);
	    variables.put("mealLabel", mealLabel);
	    
	    try {
	        sendMail.sendMail(
	            email,
	            "[미미르호텔] 다이닝 예약이 완료되었습니다.",
	            "dining_resv/dining_next_resv/dining-resv-complete",
	            variables
	        );
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	    
	    return "dining_resv/dining_next_resv/diningResvComplete";
	    
	}
	
}
