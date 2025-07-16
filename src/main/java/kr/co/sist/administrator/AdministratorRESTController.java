package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class AdministratorRESTController {

	@Autowired
	AdminService as;
	
	/**
	 * 아이디와 비밀번호 체크
	 * @param id
	 * @param pass
	 * @return
	 */
	@PostMapping("/admin/loginChk")
	public String loginChk(@RequestBody LoginDTO lDTO, HttpSession httpSession, HttpServletRequest request)throws Exception{
		boolean flag=false;
		String returnVal="fail";
		
		httpSession=request.getSession(false);
		
		
		if(httpSession != null) {
			System.out.println("id---------------"+lDTO.getId());
			System.out.println("pass---------------"+lDTO.getPass());
			
			
			//아이디가 존재 //그러면 flag값이 true겠지.
			flag=as.chkLogin(lDTO.getId(),lDTO.getPass());
			
			//System.out.println("flag----------------"+flag);
			
			if(flag) {
				//로그인 성공
				returnVal="success";
				//그럼 여기서 id, name을 session을 추가
				httpSession.setAttribute("session_id", lDTO.getId());
				httpSession.setAttribute("session_name", as.getNameById(lDTO.getId()));
		
				String session_id=(String)request.getSession().getAttribute("session_id");
				String session_name=(String)request.getSession().getAttribute("session_name");
				
				System.out.println("RestController-------"+session_id +" / "+ session_name);
				
	
			}else {
				//System.out.println("exception 발생!!!");
				throw new Exception();
				
			}
		}//end if
		//System.out.println("여기는 restController");
		return returnVal;
	}//loginChk
	
	/*직원 등록*/
	@PostMapping("/admin/registerStaff")
	public void registerStaff(@RequestBody StaffDTO staffDTO){
		
		int resultCnt=as.registerStaff(staffDTO);
		System.out.println("resultCnt======="+resultCnt);
		System.out.println("registerStaff의 staffDTO ----"+staffDTO);
		
		//return res;
	}//registerStaff

}//class
