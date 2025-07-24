package kr.co.sist.admin.resvroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.sist.resvroom.ReservationDTO;
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
			modelUtils.setFilteringInfo(model, FilterConfig.ROOM_RESV);
			modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.ROOM_RESV);
		
		
		return "admin_resv_room/admin_resv_room";
	}//adminResvRoomList
	
	@GetMapping("admin/resvroomdetail")
	public String adminResvRoomDetail(Model model, @RequestParam int resvId) {
		
		model.addAttribute("resv",ars.searchOneResv(resvId));
		
		
		return "admin_resv_room/admin_resv_room_detail";
	}//adminResvRoomDetail
	
	@GetMapping("admin/resvroommodify")
	public String adminResvRoomModify(Model model, @RequestParam int resvId) {
		ReservationDTO rDTO=ars.searchOneResv(resvId);
		model.addAttribute("resv",rDTO);
		int capacity = ars.searchCapacity(rDTO.getRoomId());
		model.addAttribute("capacity", capacity);
		return "admin_resv_room/admin_resv_room_edit";
	}//adminResvRoomModify
	
	
	@PostMapping("admin/resvModifyProcess")
	public String adminResvModifyProcess(ReservationDTO rDTO, @RequestParam int resvId,
	                                @RequestParam(required = false) String userDomain,
	                                @RequestParam(required = false) String customDomain,
	                                Model model, RedirectAttributes redirectAttrs) {
	    // 이메일 도메인 구성
	    String email = rDTO.getUserEmail();
	    if (customDomain != null && !customDomain.isEmpty()) {
	        email += "@" + customDomain;
	    } else if (userDomain != null && !userDomain.isEmpty()) {
	        email += userDomain;
	    }
	    rDTO.setUserEmail(email);

	    // 업데이트 처리
	    boolean success =  ars.modifyAdminResv(rDTO);
	   
	    if (success) {
	    	redirectAttrs.addFlashAttribute("msg", "예약 정보가 정상적으로 수정되었습니다.");
	    	redirectAttrs.addAttribute("resvId", rDTO.getResvId());
	    	
	    	return "redirect:admin/resvroomdetail";  // 수정 후 목록 페이지로 이동

	    } else {
	        redirectAttrs.addFlashAttribute("msg", "예약 수정에 실패했습니다. 다시 시도해주세요.");
	        return "redirect:admin/resvroommodify?resvId=" + rDTO.getResvId(); // 수정 페이지로 다시 이동
	    }
	}

	

}//class
