package kr.co.sist.resvroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
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
		    @RequestParam("adult") int adult,
		    @RequestParam("child") int child,
		    @RequestParam("typeName") String typeName,
		    @RequestParam("nights") int nights,
		    @RequestParam("capacity") int capacity,
		    @RequestParam("pricePerNight") int pricePerNight,
		    Model model) {
		
		 model.addAttribute("roomId", roomId);
		    model.addAttribute("checkIn", checkIn);
		    model.addAttribute("checkOut", checkOut);
		    model.addAttribute("adult", adult);
		    model.addAttribute("child", child);
		    model.addAttribute("nights", nights);
		    model.addAttribute("capacity", capacity);
		    model.addAttribute("typeName", typeName);
		    model.addAttribute("pricePerNight", pricePerNight);
		    return "room_resv/resvRoom"; // 보여줄 페이지 경로

	}//roomList
	
	@GetMapping("/searchResv")
	public String searchResv() {
		return "room_resv/searchResv";
	}//roomList
	
	@PostMapping("/room_resv/reservation")
	public String reservation(ReservationDTO rDTO, Model model) {
		//반환값 rDTO로 하고 나서 nonmemberId set하고 paymentID까지 set해야함
		
		int paySeq;
		int nonMemSeq;
		int resvSeq;
		
		System.out.println(rDTO);
		
		paySeq = ps.searchPaymentSeq();
		nonMemSeq = nms.searchNonMemberSeq();
		resvSeq = rs.searchReservationSeq();
		
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
