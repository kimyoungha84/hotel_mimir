package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	
	@Autowired(required = false)
	AdministratorMapper am;
	
	/*로그인 id 확인*/
	public boolean chkLogin(String id, String pass) {
		boolean flag=false;
		//현재 사용자가 입력한 아이디와 비밀번호가 맞는지 check
		String resultPass=am.selectEmployeeLogin(id);
		System.out.println("resultPass------------"+resultPass);
		
		
		if(pass.equals(resultPass)) {
			//비밀번호 일치
			flag=true;
		}//end if	
		
		return flag;
	}//chkLogin
	
	
}//class
