package kr.co.sist.FAQ;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import kr.co.sist.member.MemberDTO;
import kr.co.sist.member.MemberMapper;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;
import kr.co.sist.member.CustomUserDetails;

// FAQ 컨트롤러: FAQ 및 문의(채팅) 관련 기능 제공
@Controller
public class FaqController {

	@Autowired
	private FAQServiceImpl service;

	@Autowired
	private ModelUtils modelUtils;

	@Autowired
	private MemberMapper memberMapper;

	@GetMapping("/admin/chat")
	// 관리자 채팅 페이지 진입 (admin_chat.html 반환)
	public String adminChatPage() {
		
		return "inquiry/admin_chat"; // templates/admin_chat.html
	}

	@GetMapping("/admin/faq")
	// 관리자 FAQ 목록 페이지 진입 (admin_faq.html 반환)
	public String adminFAQPage(Model model) {
		
		int pageSize = 10;
		SearchController.addFragmentInfo(
				FilterConfig.FAQ,
				"inquiry/admin_faq",
				"faq_list_fm",
				"faqList"
			);
//			model.addAttribute("filterType", "dining");
//			model.addAttribute("pageSize", pageSize);
			modelUtils.setFilteringInfo(model, FilterConfig.FAQ);
			modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.FAQ);
			return "inquiry/admin_faq";
		
	}//adminFAQPage

	@GetMapping("/admin/faq/register")
	// 관리자 FAQ 등록 폼 페이지 진입
	public String faqRegisterForm() {
		
		return "inquiry/admin_faq_register"; // templates/admin_chat.html
	}//faqRegisterForm

	@GetMapping("/inquiry")
	// 사용자 문의(FAQ+채팅) 페이지 진입, 로그인 여부/사용자번호 전달
	public String inquiryPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		List<FAQDTO> faqList = service.selectAllFAQ(); // DB에서 FAQ 불러오기
		model.addAttribute("faqList", faqList);
		boolean isLogin = (userDetails != null);
		model.addAttribute("isLogin", isLogin);
		int userNum = 0;
		if (userDetails != null) {
			userNum = userDetails.getUserNum();
		}
		model.addAttribute("userNum", userNum);
		return "inquiry/inquiry"; // => templates/inquiry.html
	}//inquiryPage

	// faq 등록
	@PostMapping("/admin/faq/register")
	// FAQ 등록 처리
	public String registerFaq(@RequestParam String title, @RequestParam String content) {
		
		FAQDTO faq = new FAQDTO();
		faq.setFaq_title(title);
		// HTML 태그 제거
		
		String plainText = Jsoup.parse(content).text(); // <p>내용</p> → 내용
		faq.setFaq_content(plainText);
		faq.setFaq_date(Date.valueOf(LocalDate.now())); // 오늘 날짜 자동 설정

		service.insertFaq(faq); // DB 저장

		return "redirect:/admin/faq"; // 등록 후 리스트 페이지로 이동
	}//registerFaq

	@PostMapping("/admin/faq/delete")
	// FAQ 다중 삭제 처리 (AJAX)
	@ResponseBody
	public String deleteFaq(@RequestBody List<Integer> faqNums) {
		
		System.out.println("삭제 요청 받음: " + faqNums); // 로그 찍기
		service.deleteFaqs(faqNums);
		
		return "success";
	}//deleteFaq

	@GetMapping("/admin/faq/modify")
	// FAQ 수정 폼 페이지 진입
	public String showModifyForm(@RequestParam("faq_num") int faqNum, Model model) {
		
		FAQDTO faq = service.selectOneFaq(faqNum); // DB에서 FAQ 가져오기

		model.addAttribute("faq", faq); // HTML에 전달

		return "inquiry/admin_faq_modify"; // 수정 폼 페이지로 이동
	}//showModifyForm

	@PostMapping("/admin/faq/modify")
	// FAQ 수정 처리
	public String modifyFaq(@RequestParam("faq_num") int faqNum, @RequestParam("faq_title") String title,
			@RequestParam("faq_content") String content) {

		FAQDTO faq = new FAQDTO();
		faq.setFaq_num(faqNum);
		faq.setFaq_title(title);
		faq.setFaq_content(content); // 필요 시 Jsoup.parse(content).text() 해도 됨

		service.updateFaq(faq); // DB 업데이트 호출

		return "redirect:/admin/faq"; // 수정 후 목록으로 리다이렉트
	}//modifyFaq

}//class
