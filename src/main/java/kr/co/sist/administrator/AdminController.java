package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	
		
	
	/*비밀번호 초기화 페이지*/
	@GetMapping("/admin/resetPassword")
	public String adminResetPassword() {
		return "administrator_email_template/reset_password";
	}//adminResetPassword
	
	
	
	/*권한 없을 때 나오는 페이지*/
	@GetMapping("/admin/noAuthor")
	public String noAuthory(HttpSession session, Model model) {
		
		
		model.addAttribute("session_id",session.getAttribute("session_id"));
		model.addAttribute("session_name",session.getAttribute("session_name"));
		
		return "administrator/noAutorityPage";
	}//noAuthory
	
	


	
	

	
	
}//class
