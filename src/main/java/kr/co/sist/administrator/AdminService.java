package kr.co.sist.administrator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	
	@Autowired(required = false)
	AdministratorMapper am;
	

	
	/**
	 * 로그인할 때,
	 * 아이디와 비밀번호 check
	 * @param id
	 * @param pass
	 * @return
	 */
	public boolean chkLogin(String id, String pass) {
		boolean flag=false;
		//현재 사용자가 입력한 아이디와 비밀번호가 맞는지 check
		String resultPass=am.selectEmployeeLogin(id);
		//System.out.println("resultPass------------"+resultPass);
		
		
		if(pass.equals(resultPass)) {
			//비밀번호 일치
			flag=true;
		}//end if	
		
		return flag;
	}//chkLogin
	
	/*해당 id에 mapping되는 name 가져오기*/
	public String getNameById(String id) {
		String name=am.selectNamebyId(id);
		
		return name;
	}//getNameById
	
	
	/*해당 id에 mapping되는 permission 가져오기*/
	public List<String> getPermissionById(String id) {
		String permissions=null;
		List<String> permissionList=new ArrayList<String>();
		
		permissions=am.selectPermissionById(id);
		
		//가져온 권한 형태가 "inquiry,room" 이런 형태니까, 이걸 , 로 구분해서 list에 넣어주자
		String[] str=permissions.split(",");
		
		for(int i=0; i<str.length; i++) {
			permissionList.add(str[i]);	
		}//end for
		
		return permissionList;
	}//getPermissionById
	
	
	
	/*아이디 만들기 : 형태 mimir_6자리 랜덤숫자*/
	public String makeAdminId() {
		
		String pre_idStr="mimir_";
		String id=null;
		int randNum=0;
		String randNumStr=null;
		
		int resInt=0;
		
		boolean loopFlag=true;
		boolean idChkFlag=true;
		
		StringBuilder sb=new StringBuilder();
		
		//id생성하고, 해당 ID와 같은 id가 DB에 있는지 확인!
		while(idChkFlag) {
			while(loopFlag) {
				randNum=(int)((Math.random())*1000000);
	//			System.out.println("randNum----------------------"+randNum);
				randNumStr=randNum+"";
				
				if(randNumStr.length() != 6) {
					continue;
				}else {
					loopFlag=false;
				}//end if~else
				
			}//end while -- loopFlag
			
			sb.append(pre_idStr).append(randNumStr);
//			System.out.println("id===================="+sb.toString());
			id=sb.toString();
			
			resInt=am.selectCheckExistId(id);
//			System.out.println("resInt==================="+resInt);
			
			if(resInt != 0) {
				//같은 id가 존재!
				continue;
				//그러면 다시 loop돌아서 id값을 받아와야 한다.
			}else {
				//다른 id임!
				idChkFlag=false;
				
			}
		}//end while -- idChkFlag
		
		return id;
	}//makeAdminId

	
}//class
