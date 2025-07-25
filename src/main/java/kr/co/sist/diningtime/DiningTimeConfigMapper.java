package kr.co.sist.diningtime;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DiningTimeConfigMapper {

    List<String> selectTimeSlots(@Param("diningId") int diningId,
            					 @Param("mealType") String mealType);

    void insertTimeSlot(DiningTimeConfigDTO dto);
    
    void updateTimeSlot(@Param("configId") int configId,
            			@Param("timeSlot") String timeSlot);

    void deleteTimeSlots(@Param("diningId") int diningId,
    					 @Param("mealType") String mealType);
	
}
