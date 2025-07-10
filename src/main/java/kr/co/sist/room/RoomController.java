package kr.co.sist.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.sist.admin.room.AdminRoomService;
import kr.co.sist.filepath.FilePathService;

@Controller
public class RoomController {
	
	@Autowired
	private FilePathService fps;
	
	@Autowired
	private RoomService rs;
	
	@GetMapping("roomhome")
	public String roomHome() {
		return "room/roomHome";
	}//roomList

	@GetMapping("roomlist")
	public String roomList(Model model) {
		
		model.addAttribute("roomList",rs.searchAllRoom());
		
		return "room/room_list";
	}//roomList
	
	
	@GetMapping("roomdetail")
	public String roomDetail() {
		return "room/roomDetail";
	}//roomList
	
	
}//class
