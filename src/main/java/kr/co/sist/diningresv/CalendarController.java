package kr.co.sist.diningresv;

import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalendarController {

	@GetMapping("/calendar")
	public String showCalendar(@RequestParam(required = false) Integer year,
	                           @RequestParam(required = false) Integer month,
	                           Model model) {

	    Calendar today = Calendar.getInstance();
	    int baseYear = (year != null) ? year : today.get(Calendar.YEAR);
	    int baseMonth = (month != null) ? month : today.get(Calendar.MONTH) + 1;

	    int secondMonth = baseMonth + 1;
	    int secondYear = baseYear;
	    if (secondMonth > 12) {
	        secondMonth = 1;
	        secondYear++;
	    }

	    int prevMonth = baseMonth - 2;
	    int prevYear = baseYear;
	    if (prevMonth < 1) {
	        prevMonth += 12;
	        prevYear--;
	    }

	    int nextMonth = baseMonth + 2;
	    int nextYear = baseYear;
	    if (nextMonth > 12) {
	        nextMonth -= 12;
	        nextYear++;
	    }

	    model.addAttribute("baseYear", baseYear);
	    model.addAttribute("baseMonth", baseMonth);
	    model.addAttribute("secondYear", secondYear);
	    model.addAttribute("secondMonth", secondMonth);
	    model.addAttribute("prevYear", prevYear);
	    model.addAttribute("prevMonth", prevMonth);
	    model.addAttribute("nextYear", nextYear);
	    model.addAttribute("nextMonth", nextMonth);
	    model.addAttribute("today", today);

	    return "dining_resv/calendar";
	}
	
}  
