package kr.co.sist.dining.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;

@Controller
public class DiningController {
	@Autowired
	ModelUtils modelUtils;
	
	@GetMapping("/admin/dining")
	public String adminDining( Model model ) {
		
		int pageSize = 5; // 페이지당 항목 수

		modelUtils.setFilteringInfo(model, FilterConfig.DINING);

		modelUtils.setPageInfoAttributes(model, "admin_dining/admin_dining", "dining_list_fm", "diningList");

		modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.DINING);
		
		System.out.println("labelSelectorOptions : " + FilterConfig.DINING.getLabelSelectorOptions());

		
		return "admin_dining/admin_dining";
	}
	
	@GetMapping("/admin/stafftest")
	public String stafftest( Model model ) {
		
		int pageSize = 5; // 페이지당 항목 수

		modelUtils.setFilteringInfo(model, FilterConfig.STAFF);

		modelUtils.setPageInfoAttributes(model, "admin_dining/staff_test", "staff_list_fm", "staffList");

		modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.STAFF);
		
		return "admin_dining/staff_test";
	}
}
