package kr.co.sist.diningresv;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.dining.user.DiningDomain;

@Controller
public class DiningResvController {
	
	@Autowired
	private DiningResvService drs;
	
    @GetMapping("diningResv")
    public String diningResv(@RequestParam("diningId") int diningId, Model model) {

    	DiningDomain diningInfo = drs.searchDining(diningId);
    	
    	model.addAttribute("diningInfo", diningInfo);
    	
        List<DiningDomain> diningList = drs.searchALLDining();

        model.addAttribute("diningList", diningList);
        
        model.addAttribute("selectDiningId", diningId);
    	
        return "dining_resv/diningResv";
        
    }
    
    @GetMapping("diningResvComplete")
    public String diningResvMain() {
    	
    	return "dining_resv/dining_next_resv/diningResvComplete";
    	
    }
    
}
