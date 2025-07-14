package kr.co.sist.admin.diningresv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.diningresv.DiningResvMapper;

@Controller
public class AdminDiningResvController {

	@Autowired
	private DiningResvMapper drm;
	
    @GetMapping("adminDiningResvList")
    public String adminDiningResvList(Model model) {
    	
    	List<DiningResvDTO> list = drm.selectAllResv();
    	
    	model.addAttribute("reservationList", list);

        return "admin_diningresv/adminDiningResvList";
        
    }
	
}
