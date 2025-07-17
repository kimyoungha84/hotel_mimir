package kr.co.sist.admin.resvroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;

@Controller
public class AdminReservationController {

	@Autowired
	private AdminReservationService ars;

	@Autowired
	private ModelUtils modelUtils;
	
	@GetMapping("admin/resvroomlist")
	public String adminResvRoomList(Model model) {
		model.addAttribute("resvList",ars.searchAllResv());
		
		int pageSize = 10;
		SearchController.addFragmentInfo(
				FilterConfig.ROOM_RESV,
				"admin_resv_room/admin_resv_room",
				"resvroom_list_fm",
				"resvList"
			);
//			model.addAttribute("filterType", "dining");
//			model.addAttribute("pageSize", pageSize);
			modelUtils.setFilteringInfo(model, FilterConfig.ROOM_RESV);
			modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.ROOM_RESV);
		
		
		return "/admin_resv_room/admin_resv_room";
	}//adminResvRoomList
	
	@GetMapping("admin/resvroomdetail")
	public String adminResvRoomDetail(Model model, @RequestParam int resvId) {
		
		model.addAttribute("resv",ars.searchOneResv(resvId));
		
		
		return "/admin_resv_room/admin_resv_room_detail";
	}//adminResvRoomDetail
	
	@GetMapping("admin/resvroommodify")
	public String adminResvRoomModify() {
		return "/admin_resv_room/admin_resv_room_edit";
	}//adminResvRoomModify

}//class
