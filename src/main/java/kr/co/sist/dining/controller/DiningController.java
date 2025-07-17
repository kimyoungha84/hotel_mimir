package kr.co.sist.dining.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.dining.user.DiningDomain;
import kr.co.sist.dining.user.DiningService;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;

@Controller
public class DiningController {
	@Autowired
	ModelUtils modelUtils;
	@Autowired
	DiningService ds;
	
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
	public String diningDetail(@RequestParam("diningId") int diningId, Model model) {
		// 1. 다이닝 정보 조회 (서비스/매퍼 필요)
		DiningDomain diningInfo = ds.searchOneDining(diningId); // 예시

		// 2. 모델에 담기
		model.addAttribute("diningInfo", diningInfo);

		// 3. 뷰 반환
		return "dining/dining_detail";
	}
	
	@GetMapping("/user/user_filter")
	public String userFilter(Model model) {
		return "room/user_room_search_prototype";
	}

	@GetMapping("/admin/admin_dining_detail")
	public String adminDiningDetail(Model model) {
		
		return "dining/admin_dining_detail_editor";
	}
	

	
}
