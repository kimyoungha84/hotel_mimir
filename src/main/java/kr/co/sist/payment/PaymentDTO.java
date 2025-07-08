package kr.co.sist.payment;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString	
public class PaymentDTO {

	private int paymentId;
	private String reservationId;
	private int paymentPrice;
	private String paymentType;
	private String paymentStatus;
	private Timestamp paymentTime;
	
}//class
