package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String indexPage(HttpServletRequest request) {
		String dashboardPageStr="administrator/index";
		
		String session_id=(String)request.getSession().getAttribute("session_id");
		String authorityStr=as.getAuthoritybyID(session_id);
		
		System.out.println("dashboard=================session-id"+session_id);
		
		
		if(authorityStr.contains("room")) {
			dashboardPageStr="administrator/dashboard/room_dashboard";
		}else if(authorityStr.contains("dinning")) {
			dashboardPageStr="administrator/dashboard/dining_dashboard";
		}//end if~else
		
		return dashboardPageStr;
	}//examplePage
	
	/**
	 * 직원 관리
	 * @param model
	 * @param request
	 * @return
	 */
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
	
	/**
	 * 직원 등록
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/employee/register")
	public String employeeRegister(Model model) {
		//여기서 아이디를 만들어서 줘야 함.
		model.addAttribute("staff_id",as.makeAdminId());
		
		//System.out.println("여기는 /admin/employee/register");
		return "employee/empRegister";
	}//employeeRegister
	

	
	/**
	 * 직원 상세 post
	 * @param model
	 * @param staff_id
	 * @return
	 */
	@PostMapping("/admin/employee/detail")
	public String employeeDetail(Model model, @RequestParam String staff_id) {
		
		System.out.println("\n\n\n\n\n\n--------detail의 staff_id-------------:"+staff_id);
		//정보 부려줄 것들을 Model에 저장
		StaffDomain sd=as.getOneStaffInfo(staff_id);
		
		
		StringBuilder sb=new StringBuilder();
		sb.append(sd.getStaff_id()).append("")
		.append(sd.getStaff_name()).append(" ")
		.append(sd.getStaff_email()).append(" ")
		.append(sd.getDept_iden()).append(" ")
		.append(sd.getPosition_identified_code()).append(" ")
		.append(sd.getPermission_id_code()).append(" ")
		.append(sd.getStaff_status()).append(" ")
		.append(sd.getDate_of_employment()).append(" ");
		
		System.out.println(sb.toString());
		model.addAttribute("staffDomain",as.getOneStaffInfo(staff_id));
		
		
		return "employee/empDetail";
	}//employeeDetail
	
	
	@PostMapping("/admin/employee/modify")
	public String modifyEmployee(@RequestParam String staff_id, Model model) {
		
		StaffDomain sdomain=as.getOneStaffInfo(staff_id);
		
		model.addAttribute("staffDomain",sdomain);
		
		
		return "employee/empModify";
	}//modifyEmployee
	
	
	/*************************************/
	/*회원 관리*/
	@GetMapping("/admin/member")
	public String memberManage() {
		return "admin_member/memManage";
	}//memberManage
	

		
}//class
