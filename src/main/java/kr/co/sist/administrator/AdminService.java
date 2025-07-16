package kr.co.sist.administrator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	/*직원 등록시 초기값 설정*/
	public StaffDTO registerStaffInitSetting(StaffDTO staffDTO) {
		//1. 직원 ID, 2.직책식별 코드, 3.부서 식별 코드, 4.권한 식별 코드 리스트, 5.직원 이름, 6.직원 이메일, 7.입사일
		StaffDTO sDTO=staffDTO;
		
		//직원 상태
		sDTO.setStaff_status("ACTIVE");
		//로그파일 식별 번호
		sDTO.setLog_iden(createLogIden(staffDTO));
		//로그파일명
		sDTO.setLog_file_name(createLogFileName(staffDTO));
		//초기 비밀번호
		sDTO.setStaff_pass(createInitialPass(staffDTO));
		
		return sDTO;
	}//registerStaffInitSetting
	
	
	/*id의 randNum을 분리!*/
	public String seperateIdtoRandnum(StaffDTO staffDTO) {
		
		return staffDTO.getStaff_id().split("_")[1];
	}//seperateIdtoRandnum
	
	
	/*직원 등록*/
	@Transactional
	public int registerStaff(StaffDTO staffDTO){
		StaffDTO sDTO=registerStaffInitSetting(staffDTO);
		int resultCnt1=0;
		int resultCnt2=0;
		//여기서 DB에 insert하는 부분이 나와야지.
		//여기서 DB 연결!!!!!!!!!
		resultCnt1=am.insertLogInfo(sDTO);
		resultCnt2=am.insertStaff(sDTO);
		
		return resultCnt1+resultCnt2;
	}//registerStaff
	
	
	
	/*로그 번호 생성*/
	public String createLogIden(StaffDTO staffDTO) {
		
		return "LOG"+seperateIdtoRandnum(staffDTO);
	}//createLogIden
	
	
	/*로그 파일명 생성*/
	public String createLogFileName(StaffDTO staffDTO) {
		
		return "LOG"+seperateIdtoRandnum(staffDTO)+".txt";
	}//createLogFileName
	
	
	/*staff의 초기 비밀번호*/
	public String createInitialPass(StaffDTO staffDTO) {
		
		return "pass"+seperateIdtoRandnum(staffDTO);
	}//createInitialPass

	
}//class
