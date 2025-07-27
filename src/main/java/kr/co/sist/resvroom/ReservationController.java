package kr.co.sist.resvroom;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.mail.MessagingException;
import kr.co.sist.administrator.Util.AdministratorSendMail;
import kr.co.sist.member.CustomUserDetails;
import kr.co.sist.nonmember.NonMemberService;
import kr.co.sist.payment.PaymentService;

@Controller
public class ReservationController {

   @Autowired
   private NonMemberService nms;
   
   @Autowired
   private PaymentService ps;
   
   @Autowired
   private ReservationService rs;
   
   @GetMapping("/room_resv/resvRoom")
   public String roomList(@RequestParam("roomId") int roomId, @AuthenticationPrincipal CustomUserDetails detail,
          @RequestParam("checkIn") String checkIn,
          @RequestParam("checkOut") String checkOut,
          @RequestParam("typeName") String typeName,
          @RequestParam("bedName") String bedName,
          @RequestParam("viewName") String viewName,
          @RequestParam("nights") int nights,
          @RequestParam("capacity") int capacity,
          @RequestParam("pricePerNight") int pricePerNight,
          Model model) {
      
	   if(detail != null) {
		   
		   model.addAttribute("user",rs.loginUserData(detail.getUserNum()));
	   }
       model.addAttribute("roomId", roomId);
          model.addAttribute("checkIn", checkIn);
          model.addAttribute("checkOut", checkOut);
          model.addAttribute("nights", nights);
          model.addAttribute("capacity", capacity);
          model.addAttribute("typeName", typeName);
          model.addAttribute("bedName", bedName);
          model.addAttribute("viewName", viewName);
          model.addAttribute("pricePerNight", pricePerNight);
          return "room_resv/resvRoom"; // 보여줄 페이지 경로

   }//roomList
   
   @GetMapping("/searchResv")
   public String searchResv() {
      return "room_resv/NonMemberSearch";
   }//roomList
   
   @PostMapping("/searchResvDetail")
   public String searchResvDetail(ReservationDTO rDTO, Model model) {
	   rDTO=rs.searchReservationData(rDTO);
	   model.addAttribute("resvData",rDTO);
	   
	   return "room_resv/searchResvDetail";
   }//roomList
   
   @PostMapping("/cancelResv")
   public String cancelResv(@RequestParam String resvId) {
	   rs.cancelResv(resvId);
	   
	   return "room_resv/cancelResv";
   }//roomList
   
   @GetMapping("/room_resv/error_page")
   public String errorPage() {
       return "room_resv/error";
   }

   @Autowired
   private AdministratorSendMail sendMail;

   @PostMapping("/room_resv/reservation")
   public String reservation(ReservationDTO rDTO, RedirectAttributes redirectAttributes) {

       Integer roomId = rs.checkRoomAvailability(rDTO);
       if (roomId == null) {
           return "redirect:/room_resv/error_page";
       }

       int paySeq = ps.searchPaymentSeq();
       int resvSeq = rs.searchReservationSeq();

       rDTO.setRoomId(roomId);
       rDTO.setPaymentId(paySeq);
       rDTO.setResvId(resvSeq);
       rDTO.setStatus("예약완료");

       if (rDTO.getUserNum() == null) {
           int nonMemSeq = nms.searchNonMemberSeq();
           rDTO.setUserNum(nonMemSeq);

           nms.addNonMember(rDTO);
           ps.addPayment(rDTO);
           rs.addNonMemberReservation(rDTO);
       } else {
           ps.addPayment(rDTO);
           rs.addMemberReservation(rDTO);
       }

       String email= rDTO.getUserEmail()+rDTO.getUserDomain();
       // 📧 메일 발송
       Map<String, Object> variables = new HashMap<>();
       variables.put("resCon", rDTO);

       try {
           sendMail.sendMail(
        		   email,
               "호텔 예약이 완료되었습니다.",
               "room_resv/reservation-complete",
               variables
           );
       } catch (MessagingException e) {
           e.printStackTrace();
       }

       // 결과 페이지용 FlashAttribute
       redirectAttributes.addFlashAttribute("resCon", rDTO);

       return "redirect:/room_resv/resultResv";
   }

   @GetMapping("/room_resv/resultResv")
   public String resultResv() {
       return "room_resv/resultResv";
   }
   
   
}//class
