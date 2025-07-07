package kr.co.sist.admin.room;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminRoomController {

	@GetMapping("/admin/roomList")
	public String adminRoomList() {
		return "/admin_room/admin_room_list";
	}//adminRoomList
	
	@GetMapping("admin/roomInfoDetail")
	public String adminRoomInfoDetail() {
		return "/admin_room/admin_room_detail";
	}//roomInfoDetail
	
}//class

