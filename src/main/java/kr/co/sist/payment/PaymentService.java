package kr.co.sist.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.resvroom.ReservationDTO;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentMapper pmm;
	
	public void addPayment(ReservationDTO rDTO){
		pmm.insertPayment(rDTO);
	}//addPayment
	
	public int searchPaymentSeq(){
		
		return pmm.selectPaymentSeq();
	}//addPayment

}//class
