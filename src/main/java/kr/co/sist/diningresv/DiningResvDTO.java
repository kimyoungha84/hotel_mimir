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

	private int reservationId;
	private String reservationName;
	private String reservationTel;
	private Date reservationDate;
	private Time reservationTime;
	private int reservationCount;
	private String reservationStatus;
	private String reservationRequest;
	
	private int diningId;
	private String diningName;
	
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
