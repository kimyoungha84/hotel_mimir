package kr.co.sist.administrator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	
	
	@GetMapping("/admin")
	public String admin() {
		return "administrator/index";
	}//admin
	
	@GetMapping("/admin/login")
	public String adminLogin() {
		return "administrator/login";
	}//adminLogin
	
}//class
