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
	

    public Integer searchCapacity(int roomId) {
        return rm.selectCapacity(roomId);
    }//return sqlSession.selectOne("kr.co.sist.room.RoomMapper.getRoomCapacityByRoomId", roomId);
	
	public List<ReservationDTO> searchAllResv(){
		return rm.selectAllResv();
	}//searchAllResv
	
	public ReservationDTO searchOneResv(int resvId) {
		return rm.selectOneResv(resvId);
	}//searchOneResv
	
	
	public boolean modifyAdminResv(ReservationDTO rDTO) {
		int resvResult = rm.updateResvAdmin(rDTO);
		int nonMemResult = 0;

	    if (!rDTO.getIsMember()) { // 비회원일 경우에만
	    	nonMemResult = rm.updateNonMemberAdmin(rDTO);
	    }//end if
	    return (resvResult > 0) && (rDTO.getIsMember() ? true : nonMemResult > 0);
	}//modifyAdminResv
	
	
	
	
}//class
