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
		int statusId=0;
		String passStr="";
		
		httpSession=request.getSession(false);
		
		
		if(httpSession != null) {
			System.out.println("id---------------"+lDTO.getId());
			System.out.println("pass---------------"+lDTO.getPass());
			
			//아이디가 존재하면, 그러면, password를 가져오자.
			statusId=as.chkExistID(lDTO.getId());
			if(statusId != 0) {
				passStr=as.getPassById(lDTO.getId());
				if(passStr.contains("pass")) {
					if(as.getPassById(lDTO.getId()).equals(lDTO.getPass())) {
						returnVal="initialIssue";
					}else {
						//애초에 비밀번호가 틀린거
						throw new Exception();
					}//end if~else
				}else {
			
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
						
						//System.out.println("RestController-------"+session_id +" / "+ session_name);
						
			
					}else {
						//System.out.println("exception 발생!!!");
						throw new Exception();
						
					}//end if~else
				}//end if~else
			}//end if
		}//end if
		//System.out.println("여기는 restController");
		return returnVal;
	}//loginChk
	
	/*직원 등록*/
	@PostMapping("/admin/registerStaff")
	public int registerStaff(@RequestBody StaffDTO staffDTO) throws Exception{
		
		int resultCnt=as.registerStaff(staffDTO);
		//System.out.println("resultCnt======="+resultCnt);
		//System.out.println("registerStaff의 staffDTO ----"+staffDTO);
		if(resultCnt!=3) {
			throw new Exception();
		}else {
			//직원 등록이 완료되었을 때 실행됨.
			//직원 등록이 완료되면, password를 초기화할 수 있는 이메일 전송해야 함!
			as.sendMail(null);
		}//if~else
		

		
		return resultCnt;
	}//registerStaff
	
	/*초기 비밀번호 등록*/
	@PostMapping("/admin/initialPassword")
	public int initialPassword(@RequestBody LoginDTO lDTO) throws Exception{
		int chkIdExist=0;
		int resultStatus=0;
		String encrpyPassStr="";
		int chkValue=0;
		String passStr="";
		
		//여기서 우선 id가 db에 있는 아이디인지 확인해야함.
		chkIdExist =as.chkExistID(lDTO.getId());
		
		//그리고 이 아이디의 password 형태가 pass6자리인지 확인
		passStr=as.getPassById(lDTO.getId());
		//만약 아닐 경우에는 이게 초기 비밀번호 변경 대상이 아님.!
		if(passStr.contains("pass")) {

			//만약 db에 존재하는 아이디라면, password 변경! (이때, 암호화를 해서 집어넣어야 한다.)
			//System.out.println("------------------아이디가 존재하는가?!?!?!?!?!?=--------"+chkIdExist);
			if(chkIdExist != 0) {
				encrpyPassStr=as.encryptStr(lDTO.getPass());
				lDTO.setPass(encrpyPassStr);
				//여기서 DB로 보내야지 (암호화한 password를 변경)
				//여기서 접근하면 staff DB의 pass를 업데이트 하기
				chkValue=as.changeInitPassword(lDTO);
				if(chkValue != 0 ) {
					resultStatus=1;
				}else {
					resultStatus=0;
				}
			}else {
				//db에 존재하지 않는 아이디라면, exception 발생시키기
				throw new Exception();
			}//end if~else
		}else {
			//아이디의 형태가 초기 비밀번호 대상이 아님
			resultStatus=2;
		}
		return resultStatus;
	}//initialPassword

}//class
