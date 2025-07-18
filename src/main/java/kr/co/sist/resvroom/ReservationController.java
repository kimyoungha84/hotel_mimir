package kr.co.sist.resvroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
   public String roomList(@RequestParam("roomId") int roomId,
          @RequestParam("checkIn") String checkIn,
          @RequestParam("checkOut") String checkOut,
          @RequestParam("typeName") String typeName,
          @RequestParam("bedName") String bedName,
          @RequestParam("viewName") String viewName,
          @RequestParam("nights") int nights,
          @RequestParam("capacity") int capacity,
          @RequestParam("pricePerNight") int pricePerNight,
          Model model) {
      
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
      return "room_resv/searchResv";
   }//roomList
   
   @PostMapping("/room_resv/reservation")
   public String reservation(ReservationDTO rDTO, Model model) {
	   Integer roomId;
      //반환값 rDTO로 하고 나서 nonmemberId set하고 paymentID까지 set해야함
	   roomId=rs.checkRoomAvailability(rDTO);
	   
	   if(roomId == null  ) { return "redirect:room_resv/error"; }
      
      int paySeq;
      int nonMemSeq;
      int resvSeq;
      
      
      paySeq = ps.searchPaymentSeq();
      nonMemSeq = nms.searchNonMemberSeq();
      resvSeq = rs.searchReservationSeq();
      
      rDTO.setRoomId(roomId);
      rDTO.setUserNum(nonMemSeq);
      rDTO.setPaymentId(paySeq);
      rDTO.setResvId(resvSeq);
      rDTO.setStatus("예약완료");
      
      nms.addNonMember(rDTO);
      ps.addPayment(rDTO);
      rs.addReservation(rDTO);
      model.addAttribute("resCon", rDTO);
      
      return "room_resv/resultResv";
   }//roomList
   
}//class
