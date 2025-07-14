package kr.co.sist.admin.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.room.RoomDTO;
import kr.co.sist.room.RoomMapper;

@Service
public class AdminRoomService {
	
	@Autowired
	private RoomMapper rm;
	
	public List<RoomDTO> searchAllRoom(){
		return rm.selectAllRoom();
	}//searchAllDept
	
	public RoomDTO searchOneRoom(int roomId) {
		return rm.selectOneRoom(roomId);
	}//searchOneRoom
	
	public void modifyRoom(String typeName, String bedName, String viewName, String description) {
        rm.updateRoom(typeName, bedName, viewName, description);
    }//modifyRoom
	
	public void modifyRoomImg(String typeName, String bedName, String imagePath) {
        rm.updateRoomImg(typeName, bedName, imagePath);
    }//modifyRoomImg

	
	public Integer getRoomIdByNames(String typeName, String bedName, String viewName) {
	    return rm.findRoomIdByNames(typeName, bedName, viewName);
	}
	
}//class
