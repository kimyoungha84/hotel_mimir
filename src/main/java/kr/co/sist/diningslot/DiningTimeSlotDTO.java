package kr.co.sist.diningslot;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiningTimeSlotDTO {

    private int slotId;   
    
    private int diningId;  
    private String diningName;
    
    private Date reservationDate;  
    private Timestamp reservationTime;
    private int reservedCount;
    
    private int totalSeat;
	
}