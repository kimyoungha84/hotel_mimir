package kr.co.sist.diningslot;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DiningTimeSlotMapper {
	
	// 해당 날짜 + 시간에 예약된 인원 수 조회
    Integer selectReservedCount(@Param("diningId") int diningId,
            				@Param("reservationDate") Date reservationDate,
            				@Param("reservationTime") Timestamp reservationTime);
    
    // 슬롯 존재 여부 확인
    int existsSlot(@Param("diningId") int diningId,
    			   @Param("reservationDate") Date reservationDate,
    			   @Param("reservationTime") Timestamp reservationTime);
    
    // 슬롯 삽입
    void insertSlot(@Param("diningId") int diningId,
    				@Param("reservationDate") Date reservationDate,
    				@Param("reservationTime") Timestamp reservationTime,
    				@Param("reservedCount") int reservedCount);
    				
    // 슬롯 업데이트 (좌석 증가)
    void updateSlot(@Param("diningId") int diningId,
    				@Param("reservationDate") Date reservationDate,
    				@Param("reservationTime") Timestamp reservationTime,
    				@Param("reservedCount") int reservedCount);
    
    List<DiningTimeSlotDTO> selectAllSlots();
    
    void updateTotalSeat(@Param("slotId") int slotId,
    					 @Param("reservedCount") int reservedCount);
    
}
