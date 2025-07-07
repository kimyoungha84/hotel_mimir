package kr.co.sist.room;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

	@GetMapping("roomlist")
	public String roomList() {
		return "room/room_list";
	}//roomList
	
	
}//class
