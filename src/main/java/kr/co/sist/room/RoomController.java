package kr.co.sist.room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String roomList(
	        @RequestParam(value = "checkIn", required = false) String checkIn,
	        @RequestParam(value = "checkOut", required = false) String checkOut,
	        Model model) {

	    if (checkIn == null || checkIn.isBlank()) {
	        checkIn = LocalDate.now().toString();
	    }
	    if (checkOut == null || checkOut.isBlank()) {
	        checkOut = LocalDate.now().plusDays(1).toString();
	    }

	    RoomSearchDTO searchDTO = new RoomSearchDTO();
	    searchDTO.setCheckIn(checkIn);
	    searchDTO.setCheckOut(checkOut);

	    List<RoomDTO> roomList = rs.searchAllRoom();

	    for (RoomDTO room : roomList) {
	        RoomSearchDTO dto = new RoomSearchDTO();
	        dto.setCheckIn(checkIn);
	        dto.setCheckOut(checkOut);
	        dto.setTypeName(room.getTypeName());
	        dto.setViewName(room.getViewName());
	        dto.setBedName(room.getBedName());

	        int availableCount = rs.countAvailableRooms(dto);
	        room.setCountAvailableRooms(availableCount);
	    }

	    model.addAttribute("roomList", roomList);
	    model.addAttribute("checkIn", checkIn);
	    model.addAttribute("checkOut", checkOut);

	    return "room/room_list";
	}
	
	
	@GetMapping("roomdetail")
	public String roomDetail() {
		return "room/roomDetail";
	}//roomList
	
	
}//class
