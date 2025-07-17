package kr.co.sist.administrator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@Autowired(required = false)
	AdminService as;
	
	/**
	 * 관리자 첫 페이지
	 * session값을 불러왔을 때,<br>
	 * session_id가 없으면, login 페이지로 보낸다.<br>
	 * session_id가 있으면 첫 페이지로 보낸다.<br>
	 * @param model
	 * @return 
	 */
	@GetMapping("/admin")
	public String admin() {

	
		return "administrator/index";
	}//admin
	
	
	/**
	 * 로그인 페이지로 이동
	 * @return "administrator/login";
	 */
	@GetMapping("/admin/login")
	public String adminLogin() {
		
		return "administrator/login";
	}//adminLogin

	
	
	/*로그아웃*/
	@GetMapping("/admin/logout")
	public String logout(HttpSession session) {
		//세션 없애기.
		session.invalidate();

		return "redirect:/admin/login";
	}//logout
	
	
	/*이거 권한 가져오는건데 ..... 흠......*/
	@GetMapping("/admin/permissionChkProcess")
	public String checkPermission(String id) {
		boolean result=false;
		List<String> permissionList=as.getPermissionById(id);
		
//		if() {}
		
		return "";
	} //checkPermission
	
	@GetMapping("/admin/resetPassword")
	public String adminResetPassword() {
		return "administrator_email_template/reset_password";
	}//adminResetPassword
	
	

	/******************************************************************************/
	
	/*이건 test용*/
	@GetMapping("/test")
	public String adminTest() {
		return "administrator_email_template/reset_password";
	}//adminTest
	
	
	/*test용*/
	@GetMapping("/test2")
	public String adminTest2() {
		//testtest
		return "administrator_email_template/reset_password_info";
	}//adminTest
	

	
	

	
	
}//class
