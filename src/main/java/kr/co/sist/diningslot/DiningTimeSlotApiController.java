package kr.co.sist.diningslot;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class DiningTimeSlotApiController {

	@Autowired
	private DiningTimeSlotService dtss;
	
	@GetMapping("/remainingSeats")
	@ResponseBody
	public Map<String, Integer> getRemainingSeats(	
	        @RequestParam("diningId") int diningId,
	        @RequestParam("date") String dateStr) {

	    java.sql.Date date;
	    
	    try {
	    	
	        date = java.sql.Date.valueOf(dateStr); // yyyy-MM-dd 형식 문자열
	        
	    } catch (IllegalArgumentException e) {
	    	
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "날짜 형식이 올바르지 않습니다. (yyyy-MM-dd)");
	        
	    }

	    List<String> timeList = Arrays.asList(
	        "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
	        "15:00", "15:30", "16:00", "16:30", "17:00",
	        "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00"
	    );

	    Map<String, Integer> result = new LinkedHashMap<>();

	    for (String time : timeList) {
	    	
	        Timestamp ts = Timestamp.valueOf(dateStr + " " + time + ":00"); // "2025-08-15 12:00:00"
	        int remaining = dtss.getRemainingSeats(diningId, date, ts);
	        result.put(time, remaining);
	        
	    }

	    return result;
	    
	}
	
}