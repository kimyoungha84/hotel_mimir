package kr.co.sist.nonmember;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("nonMemberDomain")
public class NonMemberDomain {

	private int nonMemId;
	private String nonMemName;
	private String nonMemTel;
	private Date nonMemRegDate;
	private String nonMemEmail;
	
}//class
