package kr.co.sist.admin.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminRoomController {
	
	@Autowired
	private AdminRoomService ars;

	@GetMapping("/admin/roomlist")
	public String adminRoomList(Model model) {
		model.addAttribute("roomList",ars.searchAllRoom());
		
		return "/admin_room/admin_room_list";
	}//adminRoomList
	
	
	@GetMapping("admin/roomdetail")
	public String adminRoomDetail() {
		return "/admin_room/admin_room_detail";
	}//roomInfoDetail
	
	@GetMapping("admin/roomsales")
	public String adminRoomSales() {
		return "/admin_room/admin_room_sales";
	}//adminRoomSales
	
}//class

