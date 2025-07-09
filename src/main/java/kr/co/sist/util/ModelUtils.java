package kr.co.sist.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

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
    public void setPaginationAttributes(Model model, int pageSize, int currentPage, FilterConfig config) {
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", currentPage);

        // 총 항목 수 계산 (config에 따라 다르게 계산)
        int dataCount = 0;

        System.out.println(pageSize);
        System.out.println(currentPage);
        if (config != null) {
            switch (config) {
                case DINING:
                    dataCount = dss.countDining(null);  // dining 데이터 개수
                    break;
                case FAQ:
                    dataCount = dss.countFaq(null);    // faq 데이터 개수
                    System.out.println("switch-faq");
                    break;
                default : 
                	dataCount = 0;
                // 다른 필터 유형에 대한 처리 추가 가능
            }
        }

        // 모델에 totalItems 추가
        model.addAttribute("totalItems", dataCount);
    }
    
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
    	int dataCount = 0;
    	
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
    	
    	// 모델에 totalItems 추가
    	model.addAttribute("totalItems", dataCount);
    }
    
    
    public void setPaginationAttributes(Model model, int pageSize, int currentPage, int dataCount) {
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", dataCount);
    }
    
    
    
    
}
