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
}//interface
