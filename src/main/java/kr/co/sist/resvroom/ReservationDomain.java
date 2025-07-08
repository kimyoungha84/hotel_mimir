package kr.co.sist.resvroom;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReservationDomain {

	private int resvId;
	private int roomId;
	private int userNum;
	private int nonMemId;
	private int paymentId;  
	private Date checkinDate;  
	private Date checkoutDate;  
	private int numGuests;  
	private String status;  
	private Date resvRegDate;  
	private String requestMsg;  
	private int breakfast;
}//class
