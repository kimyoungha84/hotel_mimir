package kr.co.sist.administrator;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdministratorMapper {
	
	/*id를 입력값으로 넣으면, 비밀번호를 가져옴.*/
	public String selectEmployeeLogin(String employee_id);
	/*입력값 id, 출력값 name*/
	public String selectNamebyId(String employee_id);
	/*id를 입력값으로 넣으면, 해당 아이디가 가진 권한을 가져옴.*/
	public String selectPermissionById(String employee_id);
	
	/*아이디가 이미 있는 아이디인지 확인*/
	public Integer selectCheckExistId(String employee_id);
	/*한 명의 직원정보*/
	public StaffDomain selectOneStaffDetail(String employee_id);
	
	
	/*직원등록*/
	//log_record 테이블 등록 후 > staff 테이블을 등록해야 한다.
	public int insertLogInfo(StaffDTO staffDTO);
	public int insertStaffPermission(StaffDTO staffDTO);
	public int insertStaff(StaffDTO staffDTO);
	
	
	/*초기 비밀번호 수정*/
	public int updateStaffInitPassword(LoginDTO lDTO);
	/*관리자, 비밀번호 초기화 버튼 눌렀을 때*/
	public int updateResetPass(StaffDTO staffDTO);
	
	
	/*직원 수정*/
	public int deletePermission(String staff_id);
	public int insertPermission(PermissionDTO pDTO);
	public int updateStaffModify(StaffDTO sDTO);
	
	
	/*직원 퇴사*/
	public int updateStaffRetire(StaffDTO sDTO);
}//interface


