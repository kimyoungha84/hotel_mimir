package kr.co.sist.diningtime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiningTimeConfigService {

	@Autowired
	private DiningTimeConfigMapper dtcm;
	
	public List<String> getLunchTimeSlots(int diningId) {
		
	    return dtcm.selectTimeSlots(diningId, "Lunch");
	    
	}

	public List<String> getDinnerTimeSlots(int diningId) {
		
	    return dtcm.selectTimeSlots(diningId, "Dinner");
	    
	}
	
	public List<DiningTimeConfigDTO> getAllTimeSlots() {
		
	    return dtcm.selectAllTimeSlots();
	    
	}
	
	public void insertTimeSlot(DiningTimeConfigDTO dto) {
		
	    dtcm.insertTimeSlot(dto);
	    
	}

	public void deleteTimeSlot(int configId) {
		
	    dtcm.deleteTimeSlot(configId);
	    
	}
	
	public void updateTimeSlot(int configId, String timeSlot) {
		
	    dtcm.updateTimeSlot(configId, timeSlot);
	    
	}
	
    public List<String> getTimeSlots(int diningId, String mealType) {
    	
    	List<String> list = dtcm.selectTimeSlots(diningId, mealType);
        
        return list;
        
    }
	
}
