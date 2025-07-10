package kr.co.sist.admin.resvroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminReservationController {
	
	@Autowired
	private AdminReservationService ars;
	
	@GetMapping("admin/resvroomlist")
	public String adminResvRoomList(Model model) {
		model.addAttribute("resvList",ars.searchAllResv());
		return "/admin_resv_room/admin_resv_room";
	}//adminResvRoomList
	
	@GetMapping("admin/resvroomdetail")
	public String adminResvRoomDetail() {
		return "/admin_resv_room/admin_resv_room_detail";
	}//adminResvRoomDetail
	
	@GetMapping("admin/resvroommodify")
	public String adminResvRoomModify() {
		return "/admin_resv_room/admin_resv_room_edit";
	}//adminResvRoomModify

}//class
