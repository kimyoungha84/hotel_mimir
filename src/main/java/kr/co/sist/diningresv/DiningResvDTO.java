package kr.co.sist.diningresv;

import java.sql.Date;
import java.sql.Time;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiningResvDTO {

	private int diningResvId;
	private String resvName;
	private String resvTel;
	private Date resvDate;
	private Time resvTime;
	private int resvCnt;
	private String resvStatus;
	private String requestMsg;
	
	private int diningId;
	
	private int userNum;
	private String userName;
	private String userTel;
	private String userEmail;
	
	private int nonMemId;
	private String nonMemName;
	private String nonMemTel;
	
	private int paymentId;  
	private int paymentPrice;  

}
