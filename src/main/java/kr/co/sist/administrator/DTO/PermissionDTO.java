package kr.co.sist.administrator.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PermissionDTO {
	private String permission_id_code;
	private String permission_str_kor;
	private String staff_id;
}
