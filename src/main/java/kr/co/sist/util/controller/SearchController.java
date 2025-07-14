package kr.co.sist.util.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.util.FilterCondition;
import kr.co.sist.util.FilterConditionBuilder;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.domain.SearchDataDomain;
import kr.co.sist.util.service.DynamicSearchService;

@Controller
public class SearchController {
    
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    // filterType별 fragment 정보 Map
    public static final Map<String, FragmentInfo> fragmentInfoMap = new ConcurrentHashMap<>();
    static {
//        fragmentInfoMap.put("faq", new FragmentInfo("fragments/faq", "faqList", "faqList"));
//        fragmentInfoMap.put("dining", new FragmentInfo("fragments/dining", "diningList", "diningList"));
//        fragmentInfoMap.put("staff", new FragmentInfo("fragments/staff", "staffList", "staffList"));
        // 필요시 추가
    }
    /**
     * filterType별 fragment 정보 등록을 쉽게 하기 위한 static 메소드
     */
    public static void addFragmentInfo(String filterType, String fragmentTemplate, String fragmentName, String resultKey) {
        fragmentInfoMap.put(filterType, new FragmentInfo(fragmentTemplate, fragmentName, resultKey));
    }
    
    /**
     * FilterConfig enum을 사용하여 fragment 정보 등록
     */
    public static void addFragmentInfo(FilterConfig config, String fragmentTemplate, String fragmentName, String resultKey) {
        fragmentInfoMap.put(config.getFilterType(), new FragmentInfo(fragmentTemplate, fragmentName, resultKey));
    }
    public static class FragmentInfo {
        private final String fragmentTemplate;
        private final String fragmentName;
        private final String resultKey;
        public FragmentInfo(String fragmentTemplate, String fragmentName, String resultKey) {
            this.fragmentTemplate = fragmentTemplate;
            this.fragmentName = fragmentName;
            this.resultKey = resultKey;
        }
        public String getFragmentTemplate() { return fragmentTemplate; }
        public String getFragmentName() { return fragmentName; }
        public String getResultKey() { return resultKey; }
    }
    
    @Autowired
    private ModelUtils modelUtils;
    @Autowired 
    private DynamicSearchService service;
    @Autowired 
    private FilterConditionBuilder builder;

    @GetMapping("/search")
    public String search(@RequestParam MultiValueMap<String, String> params, 
                        HttpServletRequest request, 
                        Model model) {
        try {
            // filterType만 프론트에서 받음
            String filterType = params.getFirst("filterType");
            if (filterType == null || filterType.trim().isEmpty()) {
                logger.error("filterType 파라미터가 누락되었습니다.");
                throw new IllegalArgumentException("검색 타입이 지정되지 않았습니다.");
            }
            FragmentInfo fragmentInfo = fragmentInfoMap.get(filterType);
            if (fragmentInfo == null) {
                logger.error("허용되지 않은 filterType: {}", filterType);
                throw new IllegalArgumentException("허용되지 않은 filterType입니다.");
            }
            FilterConfig config = FilterConfig.fromKey(filterType);
            int offset = parseIntegerParamMulti(params, "offset", 1);
            int pageSize = parseIntegerParamMulti(params, "pageSize", 10);
            int end = offset + pageSize - 1; 
            logger.debug("검색 요청 - filterType: {}, offset: {}, end: {}", filterType, offset, end);

            List<FilterCondition> filters = builder.build(params, config);
            List<SearchDataDomain> result = service.searchByFilterConfig(config, filters, offset, end, pageSize);
            if (result == null) {
                logger.warn("검색 결과가 null입니다. filterType: {}", filterType);
                result = List.of();
            }
            model.addAttribute(fragmentInfo.getResultKey(), result);
            model.addAttribute("pageSize", pageSize);
            logger.debug("검색 완료 - 결과 개수: {}", result.size());
            return fragmentInfo.getFragmentTemplate() + " :: " + fragmentInfo.getFragmentName();
        } catch (IllegalArgumentException e) {
            logger.error("검색 파라미터 오류: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "fragments/error :: error";
        } catch (Exception e) {
            logger.error("검색 중 예상치 못한 오류 발생", e);
            model.addAttribute("error", "검색 중 오류가 발생했습니다.");
            return "fragments/error :: error";
        }
    }

    @GetMapping("/reBuildPagination")
    public String reBuildPagination(@RequestParam Map<String, String> params, Model model) {
        try {
            int pageSize = parseIntegerParam(params, "pageSize", 10);
            int totalItems = parseIntegerParam(params, "totalItems", 0);
            int currentPage = parseIntegerParam(params, "currentPage", 1);
            logger.debug("페이지네이션 재구성 - pageSize: {}, totalItems: {}, currentPage: {}", 
                       pageSize, totalItems, currentPage);
            modelUtils.setPaginationAttributes(model, pageSize, currentPage, totalItems);
            System.out.println("fragments/pagination :: pagination");
            return "fragments/pagination :: pagination";
        } catch (Exception e) {
            logger.error("페이지네이션 재구성 중 오류 발생", e);
            model.addAttribute("error", "페이지네이션 처리 중 오류가 발생했습니다.");
            return "fragments/error :: error";
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCount(@RequestParam Map<String, String> params) {
        try {
            String filterType = params.get("filterType");
            if (filterType == null || filterType.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "filterType 파라미터가 누락되었습니다."));
            }
            FilterConfig config = FilterConfig.fromKey(filterType);
            List<FilterCondition> filters = builder.build(params, config);
            int total = service.countByFilterConfig(config, filters);
            logger.debug("카운트 조회 완료 - filterType: {}, total: {}", filterType, total);
            return ResponseEntity.ok(Map.of("totalItems", total));
        } catch (IllegalArgumentException e) {
            logger.error("카운트 조회 파라미터 오류: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("카운트 조회 중 예상치 못한 오류 발생", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "카운트 조회 중 오류가 발생했습니다."));
        }
    }
    
    private int parseIntegerParam(Map<String, String> params, String key, int defaultValue) {
        try {
            String value = params.get(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("파라미터 '{}'를 정수로 변환할 수 없습니다. 기본값 {} 사용", key, defaultValue);
            return defaultValue;
        }
    }
    private int parseIntegerParamMulti(MultiValueMap<String, String> params, String key, int defaultValue) {
        try {
            String value = params.getFirst(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("파라미터 '{}'를 정수로 변환할 수 없습니다. 기본값 {} 사용", key, defaultValue);
            return defaultValue;
        }
    }

} // class
