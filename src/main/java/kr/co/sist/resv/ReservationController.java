package kr.co.sist.resv;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {

	@GetMapping("resvRoom")
	public String roomList() {
		return "room_resv/booking.html";
	}//roomList
	
	
}//class
