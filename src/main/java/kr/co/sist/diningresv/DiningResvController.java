package kr.co.sist.diningresv;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DiningResvController {
	
    @GetMapping("diningResv")
    public String diningResv() {

        return "dining_resv/diningResv";
        
    }
    
    @GetMapping("diningResvComplete")
    public String diningResvMain() {
    	
    	return "dining_resv/dining_next_resv/diningResvComplete";
    	
    }
    
}
