package kr.co.sist.admin.room;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
				/* String uploadDir = "C:/upload/room_images/"; */
	        	String uploadDir = new File("src/main/resources/static/upload/room_images/").getAbsolutePath();
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
	public String adminRoomSales(@RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
		
	    // 현재 연도 기준 기본 날짜 설정
	    LocalDate now = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    if (startDate == null) {
	        startDate = now.withMonth(1).withDayOfMonth(1).format(formatter); // 1월 1일
	    }
	    if (endDate == null) {
	        endDate = now.withMonth(12).withDayOfMonth(31).format(formatter); // 12월 31일
	    }

	    List<SalesSummaryDTO> salesList = ars.searchSalesSummary(startDate, endDate);

	    // 총합계 계산
	    int totalMember = 0, totalNonMember = 0, totalCheckout = 0, totalComplete = 0, totalCancel = 0, totalAmount = 0;

	    for (SalesSummaryDTO s : salesList) {
	        totalMember += s.getMemberCount();
	        totalNonMember += s.getNonMemberCount();
	        totalCheckout += s.getCheckoutCount();
	        totalComplete += s.getCompletedCount();
	        totalCancel += s.getCancelCount();
	        totalAmount += s.getTotalAmount();
	    }

	    Map<String, Integer> summary = new HashMap<>();
	    summary.put("totalMember", totalMember);
	    summary.put("totalNonMember", totalNonMember);
	    summary.put("totalCheckout", totalCheckout);
	    summary.put("totalComplete", totalComplete);
	    summary.put("totalCancel", totalCancel);
	    summary.put("totalAmount", totalAmount);

	    model.addAttribute("summary", summary);

	    // 현재 사용된 날짜도 다시 전달해서 input에 표시되게
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
        
		return "/admin_room/admin_room_sales";
	}//adminRoomSales
	
}//class

