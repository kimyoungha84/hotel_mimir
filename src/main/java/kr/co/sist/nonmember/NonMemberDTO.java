package kr.co.sist.nonmember;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NonMemberDTO {

	private int nonMemId;
	private String nonMemName;
	private String nonMemTel;
	private Date nonMemRegDate;
	private String nonMemEmail;
	
}//class
