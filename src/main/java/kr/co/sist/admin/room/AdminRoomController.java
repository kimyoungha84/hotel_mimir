package kr.co.sist.admin.room;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sist.room.RoomDTO;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;


@Controller
public class AdminRoomController {
	
	@Value("${upload.saveDir}")
	private String saveDir;
	
	@Autowired
	private AdminRoomService ars;
	
	@Autowired
	private ModelUtils modelUtils;

	@GetMapping("/admin/roomlist")
	public String adminRoomList(Model model) {
		model.addAttribute("roomList",ars.searchAllRoom());
		
		return "admin_room/admin_room_list";
	}//adminRoomList
	
	
	@GetMapping("admin/roomdetail")
	public String adminRoomDetail(Model model, @RequestParam int roomId) {
		model.addAttribute("room",ars.searchOneRoom(roomId));
		return "admin_room/admin_room_detail";
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
	        int maxSize=1024*1024*10;
			if(thumbnail.getSize() > maxSize) {
				throw new Exception("업로드 파일의 크기는 최대 10MByte까지만 가능합니다.");
			}//end if
	        if (thumbnail != null && !thumbnail.isEmpty()) {
				/* String uploadDir = "C:/upload/room_images/"; */
	            File dir = new File(saveDir);
	            if (!dir.exists()) dir.mkdirs();

	            String fileName = UUID.randomUUID() + "_" + thumbnail.getOriginalFilename();
	            File dest = new File(saveDir, fileName);
	            thumbnail.transferTo(dest);

	            imagePath = "/room_images/" + fileName;
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
	            return "redirect:admin/roomdetail?roomId=" + roomId;
	        }

	        ars.modifyRoom(typeName, bedName, viewName, description);

	        if (imagePath != null) {
	            ars.modifyRoomImg(typeName, bedName, imagePath);
	        }

	        Integer newRoomId = ars.getRoomIdByNames(typeName, bedName, viewName);
	        model.addAttribute("roomId", newRoomId);
	        model.addAttribute("success", true);
	        return "admin_room/admin_roomupdate_result";

	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", true);
	        return "admin_room/admin_roomupdate_result";
	    }
	}//adminRoomModifyProcess
	
	
//	@GetMapping("admin/roomsales")
//	public String adminRoomSales(@RequestParam(required = false) String startDate,
//	                             @RequestParam(required = false) String endDate,
//	                             Model model) {
//
//	    LocalDate now = LocalDate.now();
//	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//	    if (startDate == null) {
//	        startDate = now.withMonth(1).withDayOfMonth(1).format(formatter); // 1월 1일
//	    }
//	    if (endDate == null) {
//	        endDate = now.withMonth(12).withDayOfMonth(31).format(formatter); // 12월 31일
//	    }
//
//	    List<SalesSummaryDTO> salesList = ars.searchSalesSummary(startDate, endDate);
//
//	    int memberCount = 0;
//	    int nonMemberCount = 0;
//
//	    int totalMemberStay = 0;
//	    int totalNonMemberStay = 0;
//	    int totalCheckout = 0;
//	    int totalCheckin = 0;
//	    int totalComplete = 0;
//	    int totalCancel = 0;
//	    int totalStay = 0;
//	    int totalAmount = 0;
//	    int totalCheckinAmount = 0;
//	    int totalCheckoutAmount = 0;
//	    int totalCompletedAmount = 0;
//	    int totalCancelAmount = 0;
//
//	    for (SalesSummaryDTO s : salesList) {
//	        // 전체 회원/비회원 수 (모든 예약 건 포함)
//	        memberCount += s.getMemberCount();
//	        nonMemberCount += s.getNonMemberCount();
//
//	        // 체크인+체크아웃 상태 회원/비회원만 합산
//	        totalMemberStay += s.getMemberStayCount();
//	        totalNonMemberStay += s.getNonMemberStayCount();
//
//	        // 상태별 집계
//	        totalCheckout += s.getCheckoutCount();
//	        totalCheckin += s.getCheckinCount();
//	        totalComplete += s.getCompletedCount();
//	        totalCancel += s.getCancelCount();
//	        totalStay += s.getStayCount();
//	        totalAmount += s.getTotalAmount();
//	        totalCheckinAmount += s.getCheckinAmount();
//	        totalCheckoutAmount += s.getCheckoutAmount();
//	        totalCompletedAmount += s.getCompletedAmount();
//	        totalCancelAmount += s.getCancelAmount();
//	    }
//
//
//	    Map<String, Integer> summary = new HashMap<>();
//	    summary.put("totalMemberStay", totalMemberStay);
//	    summary.put("totalNonMemberStay", totalNonMemberStay);
//	    summary.put("totalCheckout", totalCheckout);
//	    summary.put("totalCheckin", totalCheckin);
//	    summary.put("totalComplete", totalComplete);
//	    summary.put("totalCancel", totalCancel);
//	    summary.put("totalStay", totalStay);
//	    summary.put("totalAmount", totalAmount);
//	    summary.put("totalCheckinAmount", totalCheckinAmount);
//	    summary.put("totalCheckoutAmount", totalCheckoutAmount);
//	    summary.put("totalCompletedAmount", totalCompletedAmount);
//	    summary.put("totalCancelAmount", totalCancelAmount);
//
//	    model.addAttribute("salesList", salesList);
//	    model.addAttribute("memberCount", memberCount);
//	    model.addAttribute("nonMemberCount", nonMemberCount);
//	    model.addAttribute("summary", summary);
//	    model.addAttribute("startDate", startDate);
//	    model.addAttribute("endDate", endDate);
//
//	    return "admin_room/admin_room_sales";
//	}//adminRoomSales
	
	@GetMapping("admin/roomsales")
	public String adminRoomSales(
	                             Model model) {

		// fragment 정보 동적 등록
		SearchController.addFragmentInfo(
			FilterConfig.ROOM_SALES,
			"admin_room/admin_room_sales",
			"fm_salesList",
			"salesList"
		);
		modelUtils.setFilteringInfo(model, FilterConfig.ROOM_SALES);
		modelUtils.setPaginationAttributes(model, 5, FilterConfig.ROOM_SALES);
		
		
		
	    return "admin_room/admin_room_sales";
	}//adminRoomSales
	
}//class

