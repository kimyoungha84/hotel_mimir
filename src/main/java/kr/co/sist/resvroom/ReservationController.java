package kr.co.sist.resvroom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

	@GetMapping("/room_resv/resvRoom")

	public String roomList(@RequestParam("roomId") int roomId,
		    @RequestParam("checkIn") String checkIn,
		    @RequestParam("checkOut") String checkOut,
		    @RequestParam("adult") int adult,
		    @RequestParam("child") int child,
		    @RequestParam("typeName") String typeName,
		    Model model) {
		
		 model.addAttribute("roomId", roomId);
		    model.addAttribute("checkIn", checkIn);
		    model.addAttribute("checkOut", checkOut);
		    model.addAttribute("adult", adult);
		    model.addAttribute("child", child);
		    model.addAttribute("typeName", typeName);

		    return "room_resv/resvRoom"; // 보여줄 페이지 경로

	}//roomList
	
	@GetMapping("searchResv")
	public String searchResv() {
		return "room_resv/searchResv";
	}//roomList
	
	
}//class
