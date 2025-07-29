package kr.co.sist.admin.diningresv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.sist.diningslot.DiningTimeSlotService;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;

@Controller
public class AdminDiningTimeSlotController {

	@Autowired
	private ModelUtils mu;

    @Autowired
    private DiningTimeSlotService dtss;
    
    @GetMapping("/admin/adminDiningResvSlot")
    public String getSlotStatusForAdmin(Model model) {
    	
    	int pageSize = 5;
    	 
        SearchController.addFragmentInfo(
                FilterConfig.DINING_SLOT,
                "admin_diningresv/adminDiningResvSlot",
                "fm_slotList",
                "slotList"
        );

        mu.setFilteringInfo(model, FilterConfig.DINING_SLOT);
        mu.setPaginationAttributes(model, pageSize, FilterConfig.DINING_SLOT);

        return "admin_diningresv/adminDiningResvSlot";
        
    }
    
    @PostMapping("/admin/updateTotalSeat")
    public String updateTotalSeat(@RequestParam int slotId,
                                  @RequestParam int totalSeat,
                                  RedirectAttributes redirectAttributes) {

        try {
        	
            dtss.updateTotalSeatOnly(slotId, totalSeat);
            
            redirectAttributes.addFlashAttribute("successMessage", "총 좌석 수가 수정되었습니다.");
            
        } catch (Exception e) {
        	
            redirectAttributes.addFlashAttribute("errorMessage", "수정 중 오류가 발생했습니다.");
            
            e.printStackTrace();
            
        }

        return "redirect:/admin/adminDiningResvSlot";
        
    }
    
    @PostMapping("/admin/deleteSlot")
    public String deleteSlot(@RequestParam int slotId, RedirectAttributes redirectAttributes) {
    	
        try {
        	
        	boolean hasReservation = dtss.hasReservationInSlot(slotId);
        	
            if (hasReservation) {
            	
                redirectAttributes.addFlashAttribute("errorMessage", "예약된 인원이 있어 삭제할 수 없습니다.");
                
            } else {
            	
                dtss.deleteSlotById(slotId);
                
                redirectAttributes.addFlashAttribute("successMessage", "좌석 슬롯이 삭제되었습니다.");
                
            }
            
        } catch (Exception e) {
        	
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 중 오류가 발생했습니다.");
            
            e.printStackTrace();
            
        }
        
        return "redirect:/admin/adminDiningResvSlot";
        
    }
    
}