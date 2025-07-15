package kr.co.sist.admin.diningresv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;

@Controller
public class AdminDiningResvController {

	@Autowired
	private ModelUtils mu;
	
	@Autowired
	private AdminDiningResvService adrs;
	
    @GetMapping("adminDiningResvList")
    public String adminDiningResvList(Model model) {
    	
    	int pageSize = 5;
    	
		SearchController.addFragmentInfo(
				FilterConfig.DINING_RESV,
				"admin_diningresv/adminDiningResvList",
				"diningResv",
				"reservationList"
			);
		
			mu.setFilteringInfo(model, FilterConfig.DINING_RESV);
			mu.setPaginationAttributes(model, pageSize, FilterConfig.DINING_RESV);

        return "admin_diningresv/adminDiningResvList";
        
    }
    
    @GetMapping("/adminDiningResvDetail/{reservationId}")
    public String adminDiningResvDetail(@PathVariable int reservationId, Model model) {
    	
    	DiningResvDTO dto = adrs.selectResvId(reservationId);
    	
    	model.addAttribute("reservationDetail", dto);
    	
    	return "admin_diningresv/adminDiningResvDetail";
    	
    }
	
}
