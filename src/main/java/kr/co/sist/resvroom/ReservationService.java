package kr.co.sist.resvroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReservationService {
	
	@Autowired
	private ReservationMapper rm;
	
	public void addReservation(ReservationDTO rDTO){
		rm.insertReservation(rDTO);
	}//addReservation
	
	public int searchReservationSeq() {
		return rm.selectReservationSeq();
	}
	
	public int checkRoomAvailability(ReservationDTO rDTO) {
		return rm.checkRoomAvailability(rDTO);
	}
	
}//class
