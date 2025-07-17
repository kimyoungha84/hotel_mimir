package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;

@Controller
public class PageConnectController {
	
	@Autowired
	ModelUtils modelUtils;
	
	@Autowired(required = false)
	AdminService as;
	
	/*dashboard*/
	@GetMapping("/admin/dashboard")
	public String examplePage() {
		return "administrator/index";
	}//examplePage
	
	/*************************************/
	/*직원 관리 페이지*/
	@GetMapping("/admin/employee")
	public String employeeManagePage(Model model,HttpServletRequest request) {
		int pageSize=5;//페이지당 항목수
		
		//fragment 정보 동적 등록
		SearchController.addFragmentInfo(
				FilterConfig.STAFF, 
				"employee/empManage", 
				"staff_list_fm", 
				"staffList"
				);
		
		//System.out.println("-----------------------FilterConfigstaff --------"+FilterConfig.STAFF);
		
		modelUtils.setFilteringInfo(model, FilterConfig.STAFF);
		modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.STAFF);
		
		return "employee/empManage";
	}//employeeManagePage
	
	/*직원 등록 페이지*/
	@GetMapping("/admin/employee/register")
	public String employeeRegister(Model model) {
		//여기서 아이디를 만들어서 줘야 함.
		model.addAttribute("staff_id",as.makeAdminId());
		
		System.out.println("여기는 /admin/employee/register");
		return "employee/empRegister";
	}//employeeRegister
	
	/*************************************/
	/*회원 관리*/
	@GetMapping("/admin/member")
	public String memberManage() {
		return "admin_member/memManage";
	}//memberManage
	
		
}//class
