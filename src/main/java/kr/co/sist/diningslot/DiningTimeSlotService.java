package kr.co.sist.diningslot;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.diningtime.DiningTimeConfigMapper;

@Service
public class DiningTimeSlotService {

	@Autowired
	private DiningTimeSlotMapper dtsm;
	
	@Autowired
	private DiningTimeConfigMapper dtcm;
	
	private static final int MAX_SEATS = 20;
	
	// 잔여좌석 계산
    public int getRemainingSeats(int diningId, Date reservationDate, Timestamp reservationTime) {
    	
        Integer reserved = dtsm.selectReservedCount(diningId, reservationDate, reservationTime);
        
        return MAX_SEATS - (reserved == null ? 0 : reserved);
        
    }
    
    // 슬롯 존재 여부 확인
    public boolean slotExists(int diningId, Date reservationDate, Timestamp reservationTime) {
    	
        return dtsm.existsSlot(diningId, reservationDate, reservationTime) > 0;
        
    }
    
    // 슬롯 삽입
    public void createSlot(int diningId, Date reservationDate, Timestamp reservationTime, int reservedCount) {
    	
        dtsm.insertSlot(diningId, reservationDate, reservationTime, reservedCount);
        
    }
    
    // 슬롯 업데이트 (예약된 인원 누적)
    public void addToSlot(int diningId, Date reservationDate, Timestamp reservationTime, int reservedCount) {
    	
        dtsm.updateSlot(diningId, reservationDate, reservationTime, reservedCount);
        
    }
	
    // 예약 인원 반영 로직 (없으면 insert, 있으면 update)
    public void handleSlot(int diningId, Date reservationDate, Timestamp reservationTime, int reservationCount) {
    	
        if (slotExists(diningId, reservationDate, reservationTime)) {
        	
            addToSlot(diningId, reservationDate, reservationTime, reservationCount);
            
        } else {
        	
            createSlot(diningId, reservationDate, reservationTime, reservationCount);
            
        }
        
    }
    
    public List<String> getTimeSlots(int diningId, String mealType) {
    	
        return dtcm.selectTimeSlots(diningId, mealType);
        
    }
    
}
