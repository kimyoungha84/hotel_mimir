package kr.co.sist.diningresv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.dining.user.DiningDomain;

@Service
public class DiningResvService {

	@Autowired
	DiningResvMapper drm;
	
	public DiningDomain searchDining(int diningId) {

		return drm.selectDiningId(diningId);
		
	}
	
	public List<DiningDomain> searchALLDining() {
		
		return drm.selectAllDining();
		
	}
	
	public DiningResvDTO selectResvId(int reservationId) {
		
		return drm.selectResvId(reservationId);
		
	}
	
	public int searchResvSeq(){
		
		return drm.selectResvSeq();
		
	}
	
    public void insertDiningResv(DiningResvDTO dto) {
    	
        drm.insertDiningResv(dto);
        
    }
	
}
