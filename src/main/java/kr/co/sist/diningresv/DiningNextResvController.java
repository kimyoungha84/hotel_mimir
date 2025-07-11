package kr.co.sist.diningresv;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiningNextResvController {
	
	@GetMapping("/diningNextResv")
	public String nextReservation(@RequestParam String dining,
	                              @RequestParam int adult,
	                              @RequestParam int child,
	                              @RequestParam String date,
	                              @RequestParam String time,
	                              @RequestParam String meal,
	                              Model model) {
		
        String formattedDate;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = inputFormat.parse(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd(E)", Locale .KOREAN);
            formattedDate = outputFormat.format(parsedDate);
        } catch (Exception e) {
            formattedDate = date;
        }
		
        String mealLabel = switch (meal) {
            case "Lunch" -> "Lunch (11:30~14:30)";
            case "Dinner" -> "Dinner (17:30~22:00)";
            default -> meal;
        };
		
	    model.addAttribute("dining", dining);
	    model.addAttribute("adult", adult);
	    model.addAttribute("child", child);
	    model.addAttribute("date", date);
	    model.addAttribute("time", time);
	    model.addAttribute("meal", meal);
	    
        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("mealLabel", mealLabel);
	    
	    return "dining_resv/dining_next_resv/diningNextResv";
	}
	
}
