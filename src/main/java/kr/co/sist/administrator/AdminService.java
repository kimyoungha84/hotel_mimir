package kr.co.sist.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;

@Service
public class AdminService {
	
	@Autowired(required = false)
	private AdministratorMapper am;
	
	@Autowired(required = false)
	private CryptographicDecryption cd;
	
	
	@Autowired
	private ApplicationContext context;
		
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
		System.out.println("resultPass------------"+resultPass);
		System.out.println("Stringpass=============="+pass);
	
		if(cd.useBcryptMatches(pass, resultPass)) {
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
	
	
	/*한 명의 직원 상세 정보 가져오기*/
	public StaffDomain getOneStaffInfo(String staff_id) {
		
		StaffDomain sd=am.selectOneStaffDetail(staff_id);
		
		sd.setDept_name(departmentMapping(sd.getDept_iden()));//부서이름
		sd.setPermission_str_kor(permissionMapping(sd.getPermission_id_code()));//권한 (이거 여러개 잖아.)
		sd.setPosition_name(positionMapping(sd.getPosition_identified_code()));//부서
		sd.setStaff_status_kor(statusMapping(sd.getStaff_status()));//재직 상태
		
		System.out.println("sd-------------------"+sd);
		
		
		return sd;
	}//end getOneStaffInfo
	
	
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
	
	/*6자리 randNum 만들기*/
	public String sixRandNum() {
		int randNum=0;
		String randNumStr="";		
		
		randNum=(int)(Math.random()*1000000);
		randNumStr=String.format("%06d", randNum); //randNum에서 앞에 0이 나왔더라도, 그걸 채워준다.
		
		return randNumStr;
	}//end randNum
	
	
	/*직원 등록*/
	@Transactional
	public int registerStaff(StaffDTO staffDTO){
		StaffDTO sDTO=registerStaffInitSetting(staffDTO);
		int returnVal=0;
		int resultCnt1=0;
		int resultCnt2=0;
		int resultCnt3=1;
		

		if(chkEmptyData(staffDTO)) {
			//값 중 하나라도 empty가 있으면
			return 0;
		}//end if
		
		
		//여기서 DB에 insert하는 부분이 나와야지.
		//여기서 DB 연결!!!!!!!!!
		resultCnt1=am.insertLogInfo(sDTO);
		resultCnt2=am.insertStaff(sDTO);
		
		//permission_id_code는 기본적으로 List 형태니까.
		//이걸 한 개씩 보내주도록 해야겠지.
		int permissionListSize=sDTO.getPermission_id_code_list().size();
		
		//System.out.println("listSize-------------------"+permissionListSize);
		
		for(int i=0; i<permissionListSize; i++) {
			//System.out.println("listValue------------"+sDTO.getPermission_id_code_list().get(i));
			sDTO.setPermission_id_code(sDTO.getPermission_id_code_list().get(i));
			returnVal+=am.insertStaffPermission(sDTO);
			
			//System.out.println("returnVal--------------------"+returnVal);
		}//end for
		
		if(returnVal == 0) {
			resultCnt3=0;
		}//end if
		
		
		System.out.println("===================resultCnt3==================================="+(resultCnt1+resultCnt2+resultCnt3));
		return resultCnt1+resultCnt2+resultCnt3;
	}//registerStaff
	

	@Autowired
	private AdministratorSendMail sendMail;
	
	/*이메일 보내기*/
	public void sendMail(String employeeEmail) {
	
		try {
			sendMail.sendMail(employeeEmail, "MIMIR 비밀번호 재설정","templates/administrator_email_template/reset_password_info.html");
			//sendMail.sendMail(employeeEmail, "MIMIR 비밀번호 재설정");
		} catch (MessagingException e) {
			e.printStackTrace();
		}//try~catch
		
	}//sendMail
	
	/*직원 비빌번호 초기화*/
	public Map<String,String> empInitPass(StaffDTO sDTO) {
		Map<String,String> passMap=new HashMap<String, String>();
		
		String passStr="pass"+sixRandNum();
		
		sDTO.setStaff_pass(passStr);//새로운 비밀번호 설정
		
		int resultVal=am.updateResetPass(sDTO);
		sendMail(sDTO.getStaff_email());//이메일 보내기
		
		passMap.put("resultUpdate",resultVal+"");
		passMap.put("newpassStr", passStr);
		
		return passMap;
	}//empInitPass
	

	

	
	
	/*일방향 암호화*/
	public String encryptStr(String plainText) {
		String cipherStr="";
		
		cipherStr=cd.useBcryptEncryption(plainText);
		
		return cipherStr;
	}//encryptStr
	
	
	
	/*현재 아이디가 있는 아이디인가?*/
	public int chkExistID(String id) {
		int chkExistid=0;
		chkExistid=am.selectCheckExistId(id);
		
		return chkExistid;
	}//chkExistID
	
	
	
	/*staff 초기 비밀번호 업데이트*/
	public int changeInitPassword(LoginDTO lDTO) {
		int changeInt=am.updateStaffInitPassword(lDTO);
		
		return changeInt;
	}//changeInitPassword
	
	
	/*아이디를 주면 비밀번호를 내보낸다.*/
	public String getPassById(String id) {
		String password=am.selectEmployeeLogin(id);
		return password;
	}//getPassById
	
	
	/*해당 권한을 가지고 있는지 확인*/
	public boolean chkHaveAuthority(String id, String uri) {
		boolean flag=false;
		
		//id를 이용해서 해당 아이디가 가진 권한을 가져오기
		//이거 mapper에 아마도 이 query가 있을 텐데....
		String idAuthoryStr=am.selectPermissionById(id);
		String urlAuthoryStr=mappingURLtoAthority(uri);
		
		System.out.println("idAuthoryStr------"+idAuthoryStr);
		System.out.println("urlAuthoryStr-------"+urlAuthoryStr);
		
		//이 url에 이 id가 접근 가능해도 되는건지?! 확인
		
		if(idAuthoryStr.contains(urlAuthoryStr) || idAuthoryStr.equals("admin")) {
			//이 url에 이 id가 접근 가능해도 되나? || 관리자 인가?
			flag=true;
		}//end if
		System.out.println("falg--------"+flag);

		return flag;
	}//chkHaveAuthority
	
	/***********************************************************************/
	
	/**
	 * 해당 문자열과 maaping되는 문자열을 반환한다.
	 * @param deptCode //부서코드
	 * @return //부서코드명
	 */
	private String departmentMapping(String deptCode) {
	  
		Map<String, String> deptMap=new HashMap<String, String>();
		deptMap.put("room", "객실");
		deptMap.put("dinning", "다이닝");
		deptMap.put("inquiry", "문의");
		deptMap.put("person", "인사");
		deptMap.put("member", "고객");
		deptMap.put("manage", "경영지원");
	    
		return deptMap.get(deptCode);
	}//departmentMapping
	
	
	/** 
	 * 직책코드에 mapping되는 문자열을 반환한다.
	 * @param positionCode //직책코드
	 * @return 직책명
	 */
	private String positionMapping(String positionCode) {
		
		Map<String, String> positionMap=new HashMap<String, String>();
		positionMap.put("A", "대표");
		positionMap.put("B", "팀장");
		positionMap.put("C", "과장");
		positionMap.put("D", "대리");
		positionMap.put("E", "사원");
		
		return positionMap.get(positionCode);
	}//positionMapping
	
	
	/**
	 * 권한코드에 mapping되는 문자열을 반환한다.
	 * @param permissionCode //권한 코드
	 * @return 권한명
	 */
	private String permissionMapping(String permissionCode) {
	
		StringBuilder sb=new StringBuilder();
		
		String permissionStr="";
		
		Map<String, String> permissionMap=new HashMap<String, String>();
		permissionMap.put("room","객실");
		permissionMap.put("dinning","다이닝");
		permissionMap.put("inquiry","문의");
		permissionMap.put("member","회원");
		permissionMap.put("employee","직원");
		permissionMap.put("admin","관리자");
		

		if(permissionCode.contains(",")) {
			//여기 들어왔다는건 권한이 여러 개라는 의미잖아.
			StringTokenizer stk=new StringTokenizer(permissionCode,",");
			System.out.println("token count ------------------"+stk.countTokens());
			
			while(stk.hasMoreTokens()) {
				sb.append(permissionMap.get(stk.nextToken())).append(",");
				
			}//end while
			
			permissionStr=sb.toString();
		}else {
			permissionStr=permissionMap.get(permissionCode);
		}//end if~else
		
		
		System.out.println("permissionStr---------------"+permissionStr);
		
		//아 여기서 정규식을 사용하면 되겠다.
		//정규식을 사용해서 맨 끝의 ,를 빼면 된다.
		if(permissionStr.endsWith(",")) {
			//만약 맨 끝에 , 가 있다면
			permissionStr.replaceAll(",$", "");
		}//end if
		
		System.out.println("permissionStrstrstr---------------"+permissionStr);
		
		
		
		return permissionStr;
	}//permissionMapping
	
	
	private String statusMapping(String statusCode) {
		
		   Map<String, String> statusMap=new HashMap<String, String>();
		   statusMap.put("ACTIVE", "재직");
		   statusMap.put("DEACTIVE", "퇴사");
		   
		   return statusMap.get(statusCode);
	}//statusMapping
	
	
	/*로그 번호 생성*/
	private String createLogIden(StaffDTO staffDTO) {
		
		return "LOG"+seperateIdtoRandnum(staffDTO);
	}//createLogIden
	
	
	/*로그 파일명 생성*/
	private String createLogFileName(StaffDTO staffDTO) {
		
		return "LOG"+seperateIdtoRandnum(staffDTO)+".txt";
	}//createLogFileName
	
	
	/*staff의 초기 비밀번호*/
	private String createInitialPass(StaffDTO staffDTO) {
		
		return "pass"+seperateIdtoRandnum(staffDTO);
	}//createInitialPass

	
	/*현재 값에 데이터 들어있는지 확인*/
	private boolean chkEmptyData(StaffDTO sDTO) {
		boolean resultVal=false;
		
		//만약에 들어와야 할 값들이 즐어오지 않았다면, 여기서 return 0;
		//staff_id, postision_identified_code, dept_iden, permission_id_code_list, staff_name, staff_email, date_of_employment
		resultVal=sDTO.getStaff_id().isEmpty();
		resultVal=sDTO.getPosition_identified_code().isEmpty();
		resultVal=sDTO.getDept_iden().isEmpty();
		resultVal=sDTO.getPermission_id_code_list().isEmpty();
		resultVal=sDTO.getStaff_name().isEmpty();
		resultVal=sDTO.getStaff_email().isEmpty();
		resultVal=sDTO.getDate_of_employment().isEmpty();
		
		
		return resultVal;
	}//chkEmptyData
	
	

	
	/*mapping URL에 해당하는 권한을 반환해준다.*/
	private String mappingURLtoAthority(String uri) {
		//페이지(URL, 권한)
		Map<String, String> accessPage=new HashMap<String, String>();
		
		accessPage.put("/admin/employee", "employee");
		accessPage.put("/admin/member","member");
		
		accessPage.put("/admin/roomlist","room");
		accessPage.put("/admin/resvroomlist","room");
		accessPage.put("/admin/roomsales","room");
		
		accessPage.put("/admin/dining","dinning");
		accessPage.put("/adminDiningResvList","dinning");
		
		accessPage.put("/admin/faq","inquiry");
		accessPage.put("/admin/chat","inquiry");
		
		//uri가 들어오면, 권한을 반환해 주는거지
		return accessPage.get(uri);
		
	}//mappingURLtoID
}//class
