package kr.co.sist.administrator;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("staffDomain")
public class StaffDomain {
	private String staff_id; //직원ID
	private String position_identified_code;//직책식별코드
	
	private String dept_iden;//부서 식별 코드
	private String dept_name;//부서명

	private List<String> permission_id_code_list;//권한 식별 코드 리스트
	
	private String log_iden;//로그식별번호
	private String log_file_name;//로그 파일 명
	
	private String staff_name;//직원 이름
	private String staff_status;//직원 상태
	private String staff_email;//직원 이메일
	private String date_of_employment;//입사일 
	private String retire_date;//퇴사일
}
