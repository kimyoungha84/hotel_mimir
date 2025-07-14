package kr.co.sist.admin.room;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sist.room.RoomDTO;


@Controller
public class AdminRoomController {
	
	@Autowired
	private AdminRoomService ars;

	@GetMapping("/admin/roomlist")
	public String adminRoomList(Model model) {
		model.addAttribute("roomList",ars.searchAllRoom());
		
		return "/admin_room/admin_room_list";
	}//adminRoomList
	
	
	@GetMapping("admin/roomdetail")
	public String adminRoomDetail(Model model, @RequestParam int roomId) {
		model.addAttribute("room",ars.searchOneRoom(roomId));
		return "/admin_room/admin_room_detail";
	}//roomInfoDetail
	
	
	@PostMapping("admin/roomModifyProcess")
	public String adminRoomModifyProcess(
	    @RequestParam("typeName") String typeName,
	    @RequestParam("bedName") String bedName,
	    @RequestParam("viewName") String viewName,
	    @RequestParam("description") String description,
	    @RequestParam("roomId") Integer roomId,
	    @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
	    Model model
	) {
	    try {
	        RoomDTO oldRoom = ars.searchOneRoom(roomId);

	        String imagePath = null;
	        if (thumbnail != null && !thumbnail.isEmpty()) {
	            String uploadDir = "C:/upload/room_images/";
	            File dir = new File(uploadDir);
	            if (!dir.exists()) dir.mkdirs();

	            String fileName = UUID.randomUUID() + "_" + thumbnail.getOriginalFilename();
	            File dest = new File(uploadDir, fileName);
	            thumbnail.transferTo(dest);

	            imagePath = "/upload/room_images/" + fileName;
	        }

	        boolean isModified = false;
	        if (!oldRoom.getDescription().equals(description)) {
	            isModified = true;
	        }
	        if (imagePath != null) {
	            isModified = true;
	        }
	        // 필요하면 typeName, bedName, viewName 비교 추가 가능

	        if (!isModified) {
	            model.addAttribute("success", false);
	            model.addAttribute("message", "변경 사항이 없습니다.");
	            model.addAttribute("roomId", oldRoom.getRoomId());
	            return "redirect:/admin/roomdetail?roomId=" + roomId;
	        }

	        ars.modifyRoom(typeName, bedName, viewName, description);

	        if (imagePath != null) {
	            ars.modifyRoomImg(typeName, bedName, imagePath);
	        }

	        Integer newRoomId = ars.getRoomIdByNames(typeName, bedName, viewName);
	        model.addAttribute("roomId", newRoomId);
	        model.addAttribute("success", true);
	        return "/admin_room/admin_roomupdate_result";

	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", true);
	        return "/admin_room/admin_roomupdate_result";
	    }
	}//adminRoomModifyProcess
	
	
	@GetMapping("admin/roomsales")
	public String adminRoomSales() {
		return "/admin_room/admin_room_sales";
	}//adminRoomSales
	
}//class

