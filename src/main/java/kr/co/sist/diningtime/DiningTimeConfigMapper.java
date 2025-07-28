package kr.co.sist.diningtime;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DiningTimeConfigMapper {

    List<String> selectTimeSlots(@Param("diningId") int diningId,
            					 @Param("mealType") String mealType);

    List<DiningTimeConfigDTO> selectAllTimeSlots();
    
    void insertTimeSlot(DiningTimeConfigDTO dto);
    
    void updateTimeSlot(@Param("configId") int configId,
            			@Param("timeSlot") String timeSlot);

    void deleteTimeSlot(@Param("configId") int configId);
    
    int countReservationsForTimeSlot(DiningTimeConfigDTO dto);
	
}
