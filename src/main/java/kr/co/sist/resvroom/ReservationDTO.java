package kr.co.sist.resvroom;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ReservationDTO {

	private int resvId;
	private int roomId;
	private String typeName;
	private Integer userNum;
	private String userName; //o
	private String userTel;//o
	private String userEmail; //o
	private String userDomain;//o
	private int paymentId;  
	private Date checkinDate;  
	private Date checkoutDate;  
	private int numGuestsChild;  
	private int numGuestsAdult;
	private String paymentType;
	private String paymentStatus;
	private String status;  
	private Date resvRegDate;  
	private String requestMsg;  //o
	private int paymentPrice;  
	private int breakfast;
	
}//class
