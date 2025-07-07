package kr.co.sist.util.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sist.util.FilterCondition;
import kr.co.sist.util.FilterConditionBuilder;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.domain.SearchDataDomain;
import kr.co.sist.util.service.DynamicSearchService;


@Controller
public class SearchController {

	
//	@Autowired 
//	private ModelUtils modelUtils;
	 
    @Autowired 
    private DynamicSearchService service;
    @Autowired 
    private FilterConditionBuilder builder;

    @GetMapping("/search")
    public String search(@RequestParam Map<String, String> params, Model model) {
        FilterConfig config = FilterConfig.fromKey(params.get("filterType"));
        int offset = Integer.parseInt(params.getOrDefault("offset", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        int end = offset + pageSize - 1; 
        
        System.out.println("offset : " + offset);
        System.out.println("end : " + end);

        List<FilterCondition> filters = builder.build(params, config);

        List<SearchDataDomain> result = switch (config) {
            case DINING -> service.searchDining(filters, offset, end, pageSize);
            case FAQ -> service.searchFaq(filters, offset, end, pageSize);
        };


        String resultKey = params.get("filter_resultKey");
        String fragmentTemplate = params.get("filter_fragmentTemplate");
        String fragmentName = params.get("filter_fragmentName");

        if (resultKey == null || fragmentTemplate == null || fragmentName == null) {
            throw new IllegalArgumentException("뷰 렌더링 정보가 누락되었습니다.");
        }

        model.addAttribute(resultKey, result);
        model.addAttribute("pageSize", pageSize);

        return fragmentTemplate + " :: " + fragmentName;
    }

    @GetMapping("/reBuildPagination")
    public String reBuildPagination(@RequestParam Map<String, String> params, Model model) {
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        int totalItems = Integer.parseInt(params.getOrDefault("totalItems", "0"));
        int currentPage = Integer.parseInt(params.getOrDefault("currentPage","1"));
        
        //마지막 파라미터 int로 오버로딩된 메소드 호출
        ModelUtils.setPaginationAttributes(model, pageSize, currentPage, totalItems);

        return "fragments/pagination :: pagination";
    }

    @GetMapping("/count")
    @ResponseBody
    public Map<String, Integer> getCount(@RequestParam Map<String, String> params) {
        FilterConfig config = FilterConfig.fromKey(params.get("filterType"));
        List<FilterCondition> filters = builder.build(params, config);

        int total = switch (config) {
            case DINING -> service.countDining(filters);
            case FAQ -> service.countFaq(filters);
        };

        return Map.of("totalItems", total); // ✅ JSON 형태로 리턴
    }


} // class
