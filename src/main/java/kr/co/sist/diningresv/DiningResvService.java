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
		
		List<DiningDomain> list = drm.selectAllDining();
		
	    for (DiningDomain dining : list) {
	    	
	        // 대표 이미지 URL 조회 후 세팅
	        String imageUrl = searchMainImage(dining.getDining_id());
	        
	        dining.setDining_main_image(imageUrl);
	        
	    }
	    
		return list;
		
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
    
	public DiningResvDTO searchResvData(DiningResvDTO dto) {
		
		return drm.selectResvData(dto);
		
	}
	
	public void cancelResv2(int reservationId) {
		
		drm.cancelResv2(reservationId);
		
	}
	
    public String searchMainImage(int diningId) {
    	
        return drm.searchMainImage(diningId);
        
    }
    
    public DiningResvDTO loginUserData(int userNum) {
    	
      DiningResvDTO dto = drm.loginUserData(userNum);
    	
    	if(dto != null) {
    		
    	  if(dto.getUserTel() != null) {
    		  
    	    String tel = dto.getUserTel().replaceAll("[^0-9]", "");
    	    
    		  if (tel.length() == 11) {
    			  
    		    tel = tel.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
    		    
    		  } else if (tel.length() == 10) {
    			  
    			  tel = tel.replaceFirst("(\\d{2,3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    			  
    		  }
    		  
    		  dto.setUserTel(tel);
    		  
    	  }
    	  
        }
    	
        return dto;
        
    }
	
}
