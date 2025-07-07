package kr.co.sist.util;

import org.springframework.ui.Model;
import kr.co.sist.util.service.DynamicSearchService;

public class ModelUtils {

    // 필터 정보 설정
    public static void setFilteringInfo(Model model, FilterConfig config) {
        model.addAttribute("filter", config);
    }

    // 페이지 정보 설정 (프래그먼트)
    public static void setPageInfoAttributes(Model model, String fragmentTemplate, String fragmentName, String resultKey) {
        model.addAttribute("filter_fragmentTemplate", fragmentTemplate);
        model.addAttribute("filter_fragmentName", fragmentName);
        model.addAttribute("filter_resultKey", resultKey);
    }

    // 페이지네이션 정보 설정 - 데이터 개수 자동 조회
    public static void setPaginationAttributes(Model model, int pageSize, int currentPage, FilterConfig config, DynamicSearchService dss) {
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", currentPage);
        int totalItems = getDataCountByConfig(config, dss);
        model.addAttribute("totalItems", totalItems);
    }

    // 페이지네이션 정보 설정 - currentPage 없이 (1로 기본값 지정)
    public static void setPaginationAttributes(Model model, int pageSize, FilterConfig config, DynamicSearchService dss) {
        setPaginationAttributes(model, pageSize, 1, config, dss);
    }

    // 페이지네이션 정보 설정 - 외부에서 데이터 개수 직접 전달
    public static void setPaginationAttributes(Model model, int pageSize, int currentPage, int dataCount) {
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", dataCount);
    }

    // config 기반 데이터 개수 조회
    private static int getDataCountByConfig(FilterConfig config, DynamicSearchService dss) {
        if (config == null || dss == null) return 0;

        return switch (config) {
            case DINING -> dss.countDining(null);
            case FAQ -> dss.countFaq(null);
            default -> 0;
        };
    }
}
