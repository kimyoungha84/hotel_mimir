package kr.co.sist.resvroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {

	@GetMapping("resvRoom")
	public String roomList() {
		return "room_resv/resvRoom";
	}//roomList
	
	@GetMapping("searchResv")
	public String searchResv() {
		return "room_resv/searchResv";
	}//roomList
	
	
}//class
