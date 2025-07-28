package kr.co.sist.diningresv;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.dining.user.DiningDomain;
import kr.co.sist.dining.user.DiningService;
import kr.co.sist.dining.user.RepMenuDomain;

@Controller
public class DiningResvController {
	
	@Autowired
	private DiningResvService drs;
	
	@Autowired
	private DiningService ds;
	
    @GetMapping("/diningResv")
    public String diningResv(
    				@RequestParam("diningId") int diningId,
                    @RequestParam(name = "adult", required = false, defaultValue = "1") int adult,
                    @RequestParam(name = "child", required = false, defaultValue = "0") int child,
    				Model model) {
    	
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
