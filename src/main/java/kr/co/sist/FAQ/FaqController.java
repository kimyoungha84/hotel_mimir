package kr.co.sist.FAQ;

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

import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;

@Controller
public class FaqController {

	@Autowired
	private FAQServiceImpl service;
	
	@Autowired
	private ModelUtils modelUtils;

	@GetMapping("/admin/chat")
	public String adminChatPage() {
		return "inquiry/admin_chat"; // templates/admin_chat.html
	}

	@GetMapping("/admin/faq")
	public String adminFAQPage(Model model) {
		List<FAQDTO> list = service.selectAllFAQ();
		
		  int pageSize = 5; // 페이지당 항목 수
	      
	       modelUtils.setFilteringInfo(model, FilterConfig.FAQ);
	       
	       modelUtils.setPageInfoAttributes(model, "inquiry/admin_faq", "faq_list_fm", "faqList");
	       
	       modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.FAQ);
	      
	       
		
		model.addAttribute("faqList", list);
		/*
		 * List<FaqDTO> faqList = null; try { faqList = service.selectAllFAQ(); } catch
		 * (Exception e) { e.printStackTrace(); } // 서비스 호출
		 * model.addAttribute("faqList", faqList); System.out.println(faqList);
		 */
		return "inquiry/admin_faq"; // templates/admin_chat.html
	}

	@GetMapping("/admin/faq/register")
	public String faqRegisterForm() {
		return "inquiry/admin_faq_register"; // templates/admin_chat.html
	}

//    @PostMapping("/admin/faq/register")
//    public String registerFaq(@RequestParam String title, @RequestParam String content) {
//        // DB 저장 로직 추가
//        return "redirect:/admin/faq"; // 등록 후 리스트 페이지로 이동
//    }
	@GetMapping("/admin/inquiry")
	public String inquriyForm() {
		return "inquiry/admin_inquiry"; // templates/admin_chat.html
	}

	@GetMapping("/inquiry")
	public String inquiryPage(Model model) {
		List<FAQDTO> faqList = service.selectAllFAQ(); // DB에서 FAQ 불러오기
		model.addAttribute("faqList", faqList);
		return "inquiry/inquiry"; // => templates/inquiry.html
	}

	// faq 등록
	@PostMapping("/admin/faq/register")
	public String registerFaq(@RequestParam String title, @RequestParam String content) {
		FAQDTO faq = new FAQDTO();
		faq.setFaq_title(title);
		// HTML 태그 제거
		String plainText = Jsoup.parse(content).text(); // <p>내용</p> → 내용
		faq.setFaq_content(plainText);
		faq.setFaq_date(Date.valueOf(LocalDate.now())); // 오늘 날짜 자동 설정

		service.insertFaq(faq); // DB 저장

		return "redirect:/admin/faq"; // 등록 후 리스트 페이지로 이동
	}
	
	@PostMapping("/admin/faq/delete")
	@ResponseBody
	public String deleteFaq(@RequestBody List<Integer> faqNums) {
		System.out.println("삭제 요청 받음: " + faqNums);  // 로그 찍기
		service.deleteFaqs(faqNums);
	    return "success";
	}

}
