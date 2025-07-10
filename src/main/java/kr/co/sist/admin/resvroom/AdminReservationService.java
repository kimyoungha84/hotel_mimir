package kr.co.sist.admin.resvroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.resvroom.ReservationDTO;
import kr.co.sist.resvroom.ReservationMapper;

@Service
public class AdminReservationService {

	@Autowired
	private ReservationMapper rm;
	
	public List<ReservationDTO> searchAllResv(){
		return rm.selectAllResv();
	}//searchAllResv
	
	
	
}//class
