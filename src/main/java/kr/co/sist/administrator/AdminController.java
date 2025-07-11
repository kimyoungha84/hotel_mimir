package kr.co.sist.administrator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("permissionList")
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
	public String adminLogin(Model model, @RequestParam(required=false) String error) {
		model.addAttribute("error",error);
		
		return "administrator/login";
	}//adminLogin
	
	
	
	/**
	 * 아이디와 비밀번호 체크
	 * @param id
	 * @param pass
	 * @return
	 */
	@PostMapping("/admin/loginChk")
	public String loginChk(String id, String pass, HttpSession httpSession) {
		String path="redirect:/admin/login?error=true";
		boolean flag=false;
		List<String> permissionList=new ArrayList<String>();
		
		System.out.println("/admin/loginChk");
		
		if(id!=null) {
			//아이디가 존재 //그러면 flag값이 true겠지.
			flag=as.chkLogin(id,pass);
		
			if(flag) {
				//로그인 성공
				path="administrator/index";
				
				//그럼 여기서 id session을 추가
				httpSession.setAttribute("session_id", id);
				
			}//end if
		}//end if
		
		return path;
	}//loginChk
	
	
	/**/
	@GetMapping("/admin/permissionChkProcess")
	public String checkPermission(String id) {
		boolean result=false;
		List<String> permissionList=as.getPermissionById(id);
		
//		if() {}
		
		return "";
	} //checkPermission
	
	
	/******************************************************************************/
	
	/*이건 test용*/
	@GetMapping("/admin/test")
	public String adminTest() {
		return "administrator/reset_password";
	}//adminTest
	
	
	/*test용*/
	@GetMapping("/admin/login2")
	public String adminTest2() {
		//testtest
		return "administrator/login";
	}//adminTest
	

	
	

	
	
}//class
