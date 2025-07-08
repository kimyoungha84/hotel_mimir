package kr.co.sist.admin.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.room.RoomDTO;
import kr.co.sist.room.RoomMapper;

@Service
public class AdminRoomService {
	
	@Autowired(required = false)
	private RoomMapper rm;
	
	public List<RoomDTO> searchAllRoom(){
		return rm.selectAllRoom();
	}//searchAllDept

}//class
