package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
	@Autowired(required = false)
	AdminService as;
	
	@GetMapping("/admin")
	public String admin() {
		return "administrator/login";
	}//admin
	
	@GetMapping("/admin/login")
	public String adminLogin(String id, String pass) {
		String path="redirect:/admin";
		boolean flag=false;
		
		
		if(!id.isEmpty()) {
			//아이디가 존재
			flag=as.chkLogin();
			
		}//end if
		
		return path;
	}//adminLogin
	
}//class
