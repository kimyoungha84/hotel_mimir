package kr.co.sist.admin.diningresv;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.diningslot.DiningTimeSlotService;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;

@Controller
@RequestMapping("/admin")
public class AdminDiningResvController {

	@Autowired
	private ModelUtils mu;
	
	@Autowired
	private AdminDiningResvService adrs;
	
	@Autowired
	private DiningTimeSlotService dtss;
	
    @GetMapping("/adminDiningResvList")
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
    	
        if (dto.getReservationRequest() == null || dto.getReservationRequest().trim().isEmpty()) {
        	
            dto.setReservationRequest("없음");
            
        }
    	
    	if (dto.getReservationTime() != null) {
    		
    		int hour = dto.getReservationTime().toLocalDateTime().toLocalTime().getHour();
    	    
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
        
        if (dto.getReservationRequest() == null || dto.getReservationRequest().trim().isEmpty()) {
        	
            dto.setReservationRequest("없음");
            
        }
        
        if (dto.getReservationStatus() == null || dto.getReservationStatus().trim().isEmpty()) {
        	
            dto.setReservationStatus("진행");
            
        }
        
        String timeFormatted = new SimpleDateFormat("HH:mm").format(dto.getReservationTime());
        
        model.addAttribute("selectedTime", timeFormatted);
        
        model.addAttribute("mealType", dto.getMealType());
        
        model.addAttribute("reservationDetail", dto);
        
        return "admin_diningresv/adminDiningResvEdit";
    }
	
    @PostMapping("/adminDiningResvEdit")
    public String updateReservation(@RequestParam int reservationId,
                                    @RequestParam String reservationName,
                                    @RequestParam String reservationTell,
                                    @RequestParam int diningId,
                                    @RequestParam String diningName,
                                    @RequestParam String reservationDate,
                                    @RequestParam String reservationTime,
                                    @RequestParam int reservationCount,
                                    @RequestParam String reservationStatus,
                                    @RequestParam(required = false) String reservationRequest) {
        try {
            // 기존 예약 조회
            DiningResvDTO oldResv = adrs.selectResvId(reservationId);

            String fullDateTime = reservationDate + " " + reservationTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Timestamp ts = new Timestamp(sdf.parse(fullDateTime).getTime());
            Date resvDate = java.sql.Date.valueOf(reservationDate);

            // 좌석 이동 처리
            if (!oldResv.getReservationDate().equals(resvDate) || !oldResv.getReservationTime().equals(ts)) {
            	
                dtss.subtractFromSlot(
                		
                    oldResv.getDiningId(),
                    oldResv.getReservationDate(),
                    oldResv.getReservationTime(),
                    oldResv.getReservationCount()
                    
                );

                dtss.handleSlot(diningId, resvDate, ts, reservationCount);

            } else if (oldResv.getReservationCount() != reservationCount) {
            	
                int delta = reservationCount - oldResv.getReservationCount();
                dtss.addToSlot(diningId, resvDate, ts, delta);
                
            }
            
            boolean wasActive = !"취소".equals(oldResv.getReservationStatus());
            boolean nowCanceled = "취소".equals(reservationStatus);

            if (wasActive && nowCanceled) {
                dtss.subtractFromSlot(diningId, resvDate, ts, reservationCount);
            }

            // 예약 정보 업데이트
            DiningResvDTO dto = new DiningResvDTO();
            
            dto.setReservationId(reservationId);
            dto.setReservationName(reservationName);
            dto.setReservationTell(reservationTell);
            dto.setDiningId(diningId);
            dto.setDiningName(diningName);
            dto.setReservationDate(resvDate);
            dto.setReservationTime(ts);
            dto.setReservationCount(reservationCount);
            dto.setReservationStatus(reservationStatus);
            dto.setReservationRequest(reservationRequest == null ? "없음" : reservationRequest);

            adrs.updateResv(dto);

            return "redirect:/admin/adminDiningResvDetail/" + reservationId;

        } catch (Exception e) {
        	
            e.printStackTrace();
            
            return "redirect:/admin/error";
            
        }
        
    }
    
}
