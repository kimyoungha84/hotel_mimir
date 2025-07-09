package kr.co.sist.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageConnectController {
	
	/*dashboard*/
	@GetMapping("/admin/dashboard")
	public String examplePage() {
		return "administrator/index";
	}//examplePage
	
	/*************************************/
	/*직원 관리*/
	@GetMapping("/admin/employee")
	public String employeeManagePage() {
		return "employee/empManage";
	}//employeeManagePage
	
	/*************************************/
	/*회원 관리*/
	@GetMapping("/admin/member")
	public String memberManage() {
		return "admin_member/memManage";
	}//memberManage
	
		
}//class
