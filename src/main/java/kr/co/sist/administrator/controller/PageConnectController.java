package kr.co.sist.administrator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageConnectController {
	
	/*dashboard*/
	@GetMapping("/admin/dashboard")
	public String examplePage() {
		return "administrator/example";
	}//examplePage
	
	/*************************************/
	/*직원 관리*/
	@GetMapping("/admin/employee")
	public String employeeManagePage() {
		return "employee/empManage";
	}//employeeManagePage
	
	/*************************************/
	/*회원 관리*/
	@GetMapping("/admin/member")
	public String memberManage() {
		return "member/memManage";
	}//memberManage
	
	
	/*************************************/
//	/*객실 관리*/
//	@GetMapping("/admin/room")
//	public String roomManage() {
//		return "";
//	}//roomManage
//	//객실 정보
//	@GetMapping("/admin/roomInfo")
//	public String roomInfo() {
//		return "";
//	}//roomInfo
//	//예약 현황
//	@GetMapping("/admin/roomReserve")
//	public String roomReservation() {
//		return "";
//	}//roomReservation
//	//매출 현황
//	@GetMapping("/admin/roomSales")
//	public String roomSalesStatus() {
//		return "";
//	}//roomSalesStatus
//	
//	/*************************************/
//	/*다이닝 관리*/
//	@GetMapping("/admin/dinning")
//	public String dinningManage() {
//		return "";
//	}//dinningManage
//	//예약 관리
//	@GetMapping("/admin/dinningReserve")
//	public String dinningReserve() {
//		return "";
//	}//dinningReserve
//	
//	
//	/*************************************/
//	/*문의 관리*/
//	@GetMapping("/admin/inquiry")
//	public String inquiryManage() {
//		return "";
//	}//inquiryManage
//	//1:1 문의
//	@GetMapping("/admin/personalManage")
//	public String personalManage() {
//		return "";
//	}//personalManage
//	//FAQ
//	@GetMapping("/admin/faq")
//	public String faq() {
//		return "";
//	}//faq
//	//실시간 문의
//	@GetMapping("/admin/realtime")
//	public String realTimeInquiry() {
//		return "";
//	}//realTimeInquiry
//		
}//class
