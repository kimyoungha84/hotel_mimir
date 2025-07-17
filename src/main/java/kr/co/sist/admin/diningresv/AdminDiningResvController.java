package kr.co.sist.admin.diningresv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    	
    	if (dto.getReservationTime() != null) {
    		
    	    int hour = dto.getReservationTime().toLocalTime().getHour();
    	    
    	    if (hour >= 12 && hour <= 17) {
    	    	
    	        dto.setMealType("Lunch(12:00 ~ 17:00)");
    	        
    	    } else if(hour >= 18 && hour <= 21) {
    	    	
    	        dto.setMealType("Dinner(18:00 ~ 21:00)");
    	        
    	    }
    	    
    	}
    	
    	model.addAttribute("reservationDetail", dto);
    	
    	return "admin_diningresv/adminDiningResvDetail";
    	
    }
    
    @GetMapping("/adminDiningResvEdit/{reservationId}")
    public String adminDiningResvEdit(@PathVariable int reservationId, Model model) {
    	
        DiningResvDTO dto = adrs.resvDetail(reservationId);
        
        model.addAttribute("reservationDetail", dto);
        
        return "admin_diningresv/adminDiningResvEdit";
    }
	
    @PostMapping("adminDiningResvEdit")
    public String updateReservation(@ModelAttribute DiningResvDTO dto) {
    	
        adrs.updateResv(dto);
        
        return "redirect:/adminDiningResvDetail/" + dto.getReservationId();
    }
    
}
