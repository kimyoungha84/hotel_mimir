package kr.co.sist.diningslot;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import kr.co.sist.diningtime.DiningTimeConfigService;

@RestController
@RequestMapping("/api/timeslots")
public class DiningTimeSlotController {

    @Autowired
    private DiningTimeConfigService dtcs;

    @Autowired
    private DiningTimeSlotService dtss;

    // 시간대 + 잔여좌석 조회
    @GetMapping
    public List<Map<String, Object>> getTimeSlots(
    							@RequestParam int diningId,
    							@RequestParam String mealType,
    							@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reservationDate) {

        List<String> timeList = dtcs.getTimeSlots(diningId, mealType);
        
        List<Map<String, Object>> result = new ArrayList<>();

        for (String timeStr : timeList) {
        	
            try {
            	
                LocalTime localTime = LocalTime.parse(timeStr);
                
                LocalDateTime dateTime = reservationDate.atTime(localTime);
                
                Timestamp timestamp = Timestamp.valueOf(dateTime);

                int remainingSeats = dtss.getRemainingSeats(diningId, Date.valueOf(reservationDate), timestamp);

                Map<String, Object> slotInfo = new HashMap<>();
                
                slotInfo.put("time", timeStr);
                
                slotInfo.put("remainingSeats", remainingSeats);
                
                result.add(slotInfo);
                
            } catch (Exception e) {
            	
                e.printStackTrace();
                
            }
            
        }

        return result;
        
    }
    
}
