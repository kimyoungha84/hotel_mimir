package kr.co.sist.admin.diningresv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.sist.dining.user.DiningDomain;
import kr.co.sist.diningresv.DiningResvService;
import kr.co.sist.diningtime.DiningTimeConfigDTO;
import kr.co.sist.diningtime.DiningTimeConfigService;

@Controller
public class AdminDiningTimeConfigController {

    @Autowired
    private DiningTimeConfigService dtcs;

    @Autowired
    private DiningResvService drs;

    @GetMapping("/admin/adminDiningTimeConfig")
    public String showTimeConfigPage(Model model) {
    	
        List<DiningDomain> diningList = drs.searchALLDining();
        List<DiningTimeConfigDTO> timeSlotList = dtcs.getAllTimeSlots();
        
        model.addAttribute("diningList", diningList);
        model.addAttribute("timeSlotList", timeSlotList);

        return "admin_diningresv/adminDiningTimeConfig";
        
    }

    @PostMapping("/admin/insertTimeSlot")
    public String insertTimeSlot(@ModelAttribute DiningTimeConfigDTO dto,
                                 RedirectAttributes ra) {

        try {
        	
            dtcs.insertTimeSlot(dto);
            
            ra.addFlashAttribute("successMessage", "시간 슬롯이 추가되었습니다.");
            
        } catch (Exception e) {
        	
            ra.addFlashAttribute("errorMessage", "시간 슬롯 추가 중 오류가 발생했습니다.");
            
            e.printStackTrace();
            
        }

        return "redirect:/admin/adminDiningTimeConfig";
        
    }

    @PostMapping("/admin/deleteTimeSlot")
    public String deleteTimeSlot(@RequestParam int configId,
                                 RedirectAttributes ra) {

        try {
        	
            dtcs.deleteTimeSlot(configId);
            
            ra.addFlashAttribute("successMessage", "시간 슬롯이 삭제되었습니다.");
            
        } catch (Exception e) {
        	
            ra.addFlashAttribute("errorMessage", "시간 슬롯 삭제 중 오류가 발생했습니다.");
            
            e.printStackTrace();
            
        }

        return "redirect:/admin/adminDiningTimeConfig";
        
    }
    
}