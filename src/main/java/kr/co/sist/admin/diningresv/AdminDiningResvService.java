package kr.co.sist.admin.diningresv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.diningresv.DiningResvMapper;

@Service
public class AdminDiningResvService {
	
	@Autowired
	private DiningResvMapper drm;
	
	public List<DiningResvDTO> selectAllResv() {
		
		return drm.selectAllResv();
		
	}
	
	public DiningResvDTO selectResvId(int reservationId) {
		
		return drm.selectResvId(reservationId);
		
	}
	
    public DiningResvDTO resvDetail(int reservationId) {
    	
        return drm.selectResvId(reservationId);
        
    }

    public void updateResv(DiningResvDTO dto) {
    	
        drm.updateResvInfo(dto);
        
    }
	
}
