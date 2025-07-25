package kr.co.sist.diningtime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config/timeslots")
public class DiningTimeConfigController {

    @Autowired
    private DiningTimeConfigService dtcs;

    @GetMapping
    public List<String> getTimeSlots(
            				@RequestParam int diningId,
            				@RequestParam String mealType) {

    	List<String> slots = dtcs.getTimeSlots(diningId, mealType);
    	
    	return slots;
        
    }
    
}
