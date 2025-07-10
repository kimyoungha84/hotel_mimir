package kr.co.sist.filepath;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.room.RoomDTO;
import kr.co.sist.room.RoomMapper;

@Service
public class AdminRoomService {
	
	@Autowired
	private FilePathMapper fp;
	
	public List<FilePathDomain> searchAllFilePath(){
		return fp.selectAllFilePath();
	}//searchAllDept

}//class
