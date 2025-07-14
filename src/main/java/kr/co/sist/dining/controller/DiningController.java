package kr.co.sist.dining.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;

@Controller
public class DiningController {
	@Autowired
	ModelUtils modelUtils;
	
	@GetMapping("/admin/dining")
	public String adminDining( Model model ) {
		int pageSize = 5; // 페이지당 항목 수

		// fragment 정보 동적 등록
		SearchController.addFragmentInfo(
			FilterConfig.DINING,
			"admin_dining/admin_dining",
			"dining_list_fm",
			"diningList"
		);
		modelUtils.setFilteringInfo(model, FilterConfig.DINING);
		modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.DINING);
		return "admin_dining/admin_dining";
	}
	
	@GetMapping("/admin/stafftest")
	public String stafftest( Model model ) {
		int pageSize = 5; // 페이지당 항목 수
		SearchController.addFragmentInfo(
			"staff",
			"admin_dining/staff_test",
			"staff_list_fm",
			"staffList"
		);
		model.addAttribute("filterType", "staff");
		model.addAttribute("pageSize", pageSize);
		modelUtils.setFilteringInfo(model, FilterConfig.STAFF);
		modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.STAFF);
		return "admin_dining/staff_test";
	}
	
	@GetMapping("/user/dining_main")
	public String diningMain(Model model) {
		
		// fragment 정보 동적 등록
				SearchController.addFragmentInfo(
					FilterConfig.DINING_USER,
					"dining/dining_main",
					"dining_list_fm",
					"diningList"
				);
				modelUtils.setFilteringInfo(model, FilterConfig.DINING_USER);
		
		
		return "dining/dining_main";
	}
	
	@GetMapping("/user/dining_detail")
	public String diningDetail(Model model) {
		return "dining/dining_detail";
	}
	
	@GetMapping("/user/user_filter")
	public String userFilter(Model model) {
		return "room/user_room_search_prototype";
	}
	
}
