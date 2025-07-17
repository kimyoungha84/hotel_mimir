package kr.co.sist.dining.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiningService {

	@Autowired
	DiningMapper diningMapper;
	
	
	public DiningDomain searchOneDining(int diningId) {
		
		System.out.println(diningMapper.selectDiningInfo(diningId));

		return diningMapper.selectDiningInfo(diningId);
		
	}
}
