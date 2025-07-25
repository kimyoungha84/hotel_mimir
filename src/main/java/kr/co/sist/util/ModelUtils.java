package kr.co.sist.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import kr.co.sist.util.domain.SearchDataDomain;
import kr.co.sist.util.service.DynamicSearchService;


@Component  // Spring 빈으로 등록
public class ModelUtils {
	
	
    private final DynamicSearchService dss;

    @Autowired
    public ModelUtils(DynamicSearchService dss) {
        this.dss = dss;
    }

    // 필터링 정보 설정
    /**
     * @param model
     * @param config 모델 속성이름 : filter
     */
    public void setFilteringInfo(Model model, FilterConfig config) {
    	 System.out.println("FilterConfig passed to model: " + config);
		 System.out.println("filter: " + config);
        model.addAttribute("filter", config);
        model.addAttribute("filterType", config.getFilterType());
    }

    // 페이지네이션 정보 설정
    /**
     * @param model
     * @param fragmentTemplate 모델 속성이름 : filter_fragmentTemplate
     * @param fragmentName 모델 속성이름 : filter_fragmentName
     * @param resultKey 모델 속성이름 : filter_resultKey
     */
    public void setPageInfoAttributes(Model model, String fragmentTemplate, String fragmentName, String resultKey) {
    	model.addAttribute("filter_fragmentTemplate", fragmentTemplate);
    	model.addAttribute("filter_fragmentName", fragmentName);
        model.addAttribute("filter_resultKey", resultKey);
        
    }

    // 페이지네이션 및 총 항목 수 설정
    /**
     * @param model
     * @param pageSize 모델 속성이름 : pageSize
     * @param currentPage 모델 속성이름 : currentPage
     * config로 데이터 개수를 구해서 모델 attribute에 add -> 모델 속성이름 : totalItems
     */
//    public void setPaginationAttributes(Model model, int pageSize, int currentPage, FilterConfig config) {
//        model.addAttribute("pageSize", pageSize);
//        model.addAttribute("currentPage", currentPage);
//
//        // 총 항목 수 계산 (config에 따라 다르게 계산)
//        int dataCount = 0;
//
//        System.out.println(pageSize);
//        System.out.println(currentPage);
//        if (config != null) {
//            switch (config) {
//                case DINING:
//                    dataCount = dss.countDining(null);  // dining 데이터 개수
//                    break;
//                case FAQ:
//                    dataCount = dss.countFaq(null);    // faq 데이터 개수
//                    System.out.println("switch-faq");
//                    break;
//                default : 
//                	dataCount = 0;
//                // 다른 필터 유형에 대한 처리 추가 가능
//            }
//        }
//
//        // 모델에 totalItems 추가
//        model.addAttribute("totalItems", dataCount);
//    }
    
    /**
     * @param model
     * @param pageSize 모델 속성이름 : pageSize
     * @param config 모델 속성이름 : filter
     */
    public void setPaginationAttributes(Model model, int pageSize, FilterConfig config) {
    	model.addAttribute("pageSize", pageSize);
    	int initialCurrentPage = 1;
    	model.addAttribute("currentPage", initialCurrentPage);
    	
    	// 총 항목 수 계산 (config에 따라 다르게 계산)
    	int totalItems = 0;
    	
    	System.out.println(pageSize);
    	System.out.println(initialCurrentPage);
//    	if (config != null) {
//    		switch (config) {
//    		case DINING:
//    			dataCount = dss.countDining(null);  // dining 데이터 개수
//    			break;
//    		case FAQ:
//    			dataCount = dss.countFaq(null);    // faq 데이터 개수
//    			System.out.println("switch-faq");
//    			break;
//    		default : 
//    			dataCount = 0;
//    			// 다른 필터 유형에 대한 처리 추가 가능
//    		}
//    	}
    	
    	
    	if (config != null) {
    		dss.countByFilterConfig(config, null);
    	}
    	
    	 int totalPages = (int) Math.ceil((double) totalItems / pageSize);
         if (totalPages == 0) totalPages = 1; // 최소 1페이지는 보장
         model.addAttribute("totalPages", totalPages);
    	
    	// 모델에 totalItems 추가
    	model.addAttribute("totalItems", totalItems);
    }
    
    
    public void setPaginationAttributes(Model model, int pageSize, int currentPage, int totalItems) {
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages == 0) totalPages = 1; // 최소 1페이지는 보장
        model.addAttribute("totalPages", totalPages);
    }
    
    public void setRoomSalesModel(Model model, List<SearchDataDomain> domain, String startDate, String endDate) {
    	
    	LocalDate now = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    if (startDate == null) {
	        startDate = now.withMonth(1).withDayOfMonth(1).format(formatter); // 1월 1일
	    }
	    if (endDate == null) {
	        endDate = now.withMonth(12).withDayOfMonth(31).format(formatter); // 12월 31일
	    }

	    List<SearchDataDomain> salesList = domain;

	    int memberCount = 0;
	    int nonMemberCount = 0;

	    int totalMemberStay = 0;
	    int totalNonMemberStay = 0;
	    int totalCheckout = 0;
	    int totalCheckin = 0;
	    int totalComplete = 0;
	    int totalCancel = 0;
	    int totalStay = 0;
	    int totalAmount = 0;
	    int totalCheckinAmount = 0;
	    int totalCheckoutAmount = 0;
	    int totalCompletedAmount = 0;
	    int totalCancelAmount = 0;

	    for (SearchDataDomain s : salesList) {
	        // 전체 회원/비회원 수 (모든 예약 건 포함)
	        memberCount += s.getMemberCount();
	        nonMemberCount += s.getNonMemberCount();

	        // 체크인+체크아웃 상태 회원/비회원만 합산
	        totalMemberStay += s.getMemberStayCount();
	        totalNonMemberStay += s.getNonMemberStayCount();

	        // 상태별 집계
	        totalCheckout += s.getCheckoutCount();
	        totalCheckin += s.getCheckinCount();
	        totalComplete += s.getCompletedCount();
	        totalCancel += s.getCancelCount();
	        totalStay += s.getStayCount();
	        totalAmount += s.getTotalAmount();
	        totalCheckinAmount += s.getCheckinAmount();
	        totalCheckoutAmount += s.getCheckoutAmount();
	        totalCompletedAmount += s.getCompletedAmount();
	        totalCancelAmount += s.getCancelAmount();
	    }


	    Map<String, Integer> summary = new HashMap<>();
	    summary.put("totalMemberStay", totalMemberStay);
	    summary.put("totalNonMemberStay", totalNonMemberStay);
	    summary.put("totalCheckout", totalCheckout);
	    summary.put("totalCheckin", totalCheckin);
	    summary.put("totalComplete", totalComplete);
	    summary.put("totalCancel", totalCancel);
	    summary.put("totalStay", totalStay);
	    summary.put("totalAmount", totalAmount);
	    summary.put("totalCheckinAmount", totalCheckinAmount);
	    summary.put("totalCheckoutAmount", totalCheckoutAmount);
	    summary.put("totalCompletedAmount", totalCompletedAmount);
	    summary.put("totalCancelAmount", totalCancelAmount);

//	    model.addAttribute(resultKey, salesList);
	    model.addAttribute("memberCount", memberCount);
	    model.addAttribute("nonMemberCount", nonMemberCount);
	    model.addAttribute("summary", summary);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
    	
    }
    
    
    
    
}
