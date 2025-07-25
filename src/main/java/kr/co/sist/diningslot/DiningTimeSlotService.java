package kr.co.sist.diningslot;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiningTimeSlotService {

	@Autowired
	private DiningTimeSlotMapper dtsm;
	
    public int getRemainingSeats(int diningId, Date reservationDate, Timestamp reservationTime) {
    	
        Integer reserved = dtsm.selectReservedCount(diningId, reservationDate, reservationTime);
        
        if( reserved == null ) {
        	
        	reserved = 0;
        	
        }
        
        int totalSeats = 20; // 고정 좌석 수
        
        int remaining = totalSeats - reserved;

        return remaining;
        
    }
    
    public void reserveSeats(int diningId, Date reservationDate, Timestamp reservationTime, int reservationCount) {
    	
        int exists = dtsm.existsSlot(diningId, reservationDate, reservationTime);

        if (exists == 0) {
        	
            dtsm.insertSlot(diningId, reservationDate, reservationTime, reservationCount);
            
        } else {
        	
            dtsm.updateSlot(diningId, reservationDate, reservationTime, reservationCount);
            
        }
        
    }
	
}
