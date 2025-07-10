package kr.co.sist.administrator;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdministratorMapper {
	public String selectEmployeeLogin(String employee_id);
}//interface
