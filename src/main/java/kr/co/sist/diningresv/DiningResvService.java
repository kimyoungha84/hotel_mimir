package kr.co.sist.diningresv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiningResvService {
	
	@Autowired(required = false)
	private DiningResvMapper drm;
	
	public List<DiningResvDTO> selectAllResv() {
		
		return drm.selectAllResv();
		
	};
	
	
}
