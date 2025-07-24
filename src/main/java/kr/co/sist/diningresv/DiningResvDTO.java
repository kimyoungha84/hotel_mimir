package kr.co.sist.diningresv;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiningResvDTO {

	private int reservationId;
	private String reservationName;
	private String reservationTell;
	private String reservationEmail;
	private Date reservationDate;
	private Timestamp reservationTime;
	private int reservationCount;
	private String reservationStatus;
	private String reservationRequest;
	private String reservationType;
	
	private String mealType;
	
	private int diningId;
	private String diningName;
	
	private Integer userNum;
	private String userName;
	private String userTel;
	private String userEmail;
	
	private Integer nonMemId;
	private String nonMemName;
	private String nonMemTel;
	private String nonMemEmail;
	
	private int paymentId;  
	private int paymentPrice;  
	private String paymentType;  
	private String paymentStatus;  

}
