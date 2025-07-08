package kr.co.sist.resvroom;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Alias("resvRoomDTO")
@Getter
@Setter
@ToString
public class JoinReservationDTO {

	private int resvId;
	private int roomId;
	private String typeName;
	private int userNum;
	private String userName;
	private String userTel;
	private String userEmail;
	private int nonMemId;
	private String nonMemName;
	private String nonMemTel; 
	private int paymentId;  
	private Date checkinDate;  
	private Date checkoutDate;  
	private int numGuests;  
	private String status;  
	private Date resvRegDate;  
	private String requestMsg;  
	private int paymentPrice;  
	private int breakfast;
	
}//class
