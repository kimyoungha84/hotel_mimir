package kr.co.sist.room;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {
	
	@GetMapping("roomhome")
	public String roomHome() {
		return "room/roomHome";
	}//roomList

	@GetMapping("roomlist")
	public String roomList() {
		return "room/room_list";
	}//roomList
	
	
	@GetMapping("roomdetail")
	public String roomDetail() {
		return "room/roomDetail";
	}//roomList
	
	
}//class
