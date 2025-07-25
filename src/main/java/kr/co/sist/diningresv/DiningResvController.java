package kr.co.sist.diningresv;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.dining.user.DiningDomain;
import kr.co.sist.dining.user.DiningService;
import kr.co.sist.dining.user.RepMenuDomain;
import kr.co.sist.diningslot.DiningTimeSlotService;

@Controller
public class DiningResvController {
	
	@Autowired
	private DiningResvService drs;
	
	@Autowired
	private DiningTimeSlotService dtss;
	
	@Autowired
	private DiningService ds;
	
    @GetMapping("diningResv")
    public String diningResv(
    				@RequestParam("diningId") int diningId,
                    @RequestParam(name = "adult", required = false, defaultValue = "1") int adult,
                    @RequestParam(name = "child", required = false, defaultValue = "0") int child,
                    @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date selectedDate,
    				Model model) {

    	List<String> timeList = Arrays.asList(
    			  "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00",
    			  "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00"
    			);
    	
    	List<String> lunchTimeList = timeList.subList(0, 11);
    	List<String> dinnerTimeList = timeList.subList(11, timeList.size());

    	Map<String, Integer> remainingSeatMap = new LinkedHashMap<>();
    	
        // 기본값 : 오늘 날짜
        Date targetDate = selectedDate != null ? selectedDate : new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
    			for (String timeStr : timeList) {
    				
    			    String dateTimeStr = sdf.format(targetDate) + " " + timeStr + ":00";
    			    Timestamp timeSlot = Timestamp.valueOf(dateTimeStr);

    			    int remaining = dtss.getRemainingSeats(diningId, targetDate, timeSlot);

    			    remainingSeatMap.put(timeStr, remaining);
    			}

    			model.addAttribute("remainingSeatMap", remainingSeatMap);
    			
    			model.addAttribute("lunchTimeList", lunchTimeList);
    			model.addAttribute("dinnerTimeList", dinnerTimeList);
    	
        // 메인 이미지 URL 조회
        String mainImageUrl = drs.searchMainImage(diningId);
        
        model.addAttribute("mainImageUrl", mainImageUrl);
    	
    	DiningDomain diningInfo = drs.searchDining(diningId);
    	
    	List<RepMenuDomain> menuList = ds.searchRepMenu(diningId);
    	
    	model.addAttribute("menuList", menuList);
    	
    	model.addAttribute("diningInfo", diningInfo);
    	
        List<DiningDomain> diningList = drs.searchALLDining();

        model.addAttribute("diningList", diningList);
        
        model.addAttribute("selectDiningId", diningId);
        
        model.addAttribute("adult", adult);
        model.addAttribute("child", child);
    	
        return "dining_resv/diningResv";
        
    }
    
	@PostMapping("/searchDiningResvDetail")
	public String searchResvDetail(DiningResvDTO dto, Model model) {
		
	  dto = drs.searchResvData(dto);
	  
	    if (dto != null && (dto.getReservationRequest() == null || dto.getReservationRequest().trim().isEmpty())) {
	    	
	        dto.setReservationRequest("없음");
	        
	    }
	  
	  model.addAttribute("resvData", dto);
		   
	  return "dining_resv/searchDiningResvDetail";
	  
	}
	
	 @PostMapping("/cancelResv2")
	 public String cancelResv2(@RequestParam int reservationId) {
		 
	   drs.cancelResv2(reservationId);
		   
	   return "dining_resv/cancelResv2";
	   
	 }
    
    @GetMapping("/diningResvComplete")
    public String diningResvComplete(@RequestParam int reservationId, Model model) {
    	
        DiningResvDTO dto = drs.selectResvId(reservationId);
        
        model.addAttribute("reservationId", dto.getReservationId());
        model.addAttribute("reservationName", dto.getReservationName());
        model.addAttribute("reservationTell", dto.getReservationTell());
        model.addAttribute("dining", dto.getDiningName());
        model.addAttribute("reservationCount", dto.getReservationCount());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd(E)", Locale.KOREAN);
        String formattedDate = sdf.format(dto.getReservationDate());

        model.addAttribute("reservationDate", formattedDate);
        model.addAttribute("reservationTime", dto.getReservationTime().toString());

        return "dining_resv/dining_next_resv/diningResvComplete";
        
    }
    
}
