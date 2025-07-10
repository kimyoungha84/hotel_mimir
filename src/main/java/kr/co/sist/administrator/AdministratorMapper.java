package kr.co.sist.administrator;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdministratorMapper {
	public String selectEmployeeLogin(String employee_id);
	public List<String> selectPermissionById(String employee_id);
}//interface
