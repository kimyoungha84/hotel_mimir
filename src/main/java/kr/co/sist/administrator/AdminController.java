package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String admin(Model model) {
		String path="administrator/index";
		
		String session_id=(String)model.getAttribute("session_id");
		System.out.println("session_id----------"+session_id);
		if(session_id==null) {
			System.out.println("들어오나?!");
			path="forward:/admin/login";
		}//end if
		
		System.out.println("path---------------"+path);
		
		return path;
	}//admin
	
	
	/**
	 * 로그인 페이지로 이동
	 * @return "administrator/login";
	 */
	@GetMapping("/admin/login")
	public String adminLogin() {
		
		return "administrator/login";
	}//adminLogin
	
	
	
	@PostMapping("/admin/loginChk")
	public String loginChk(String id, String pass) {
		String path="";
		boolean flag=false;
		
		if(id!=null) {
			//아이디가 존재 //그러면 flag값이 true겠지.
			flag=as.chkLogin(id,pass);
			System.out.println("id_flag---------"+flag);
			if(flag) {
				path="administrator/index";
			}//end if
		}//end if
		System.out.println("loginChk return path -=------------------"+path);
		return path;
	}//loginChk
	
	
	/******************************************************************************/
	
	/*이건 test용*/
	@GetMapping("/admin/test")
	public String adminTest() {
		return "administrator/reset_password";
	}//adminTest
	
	
	/*test용*/
	@GetMapping("/admin/login2")
	public String adminTest2() {
		return "administrator/login";
	}//adminTest
	

	
	

	
	
}//class
