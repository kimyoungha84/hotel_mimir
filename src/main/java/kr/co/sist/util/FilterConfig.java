package kr.co.sist.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum FilterConfig {

    DINING(
        "dining", true, true, true, true,
        "diningRegDate",     // 👉 form name
        "dining_reg_date",   // 👉 실제 컬럼명
        List.of(
            new Option("name", "이름", "dining_name")
        )
    ),

    FAQ(
        "faq", true, true, true, true,
        "faqRegDate",
        "faq_date",
        List.of(
        		new Option("title", "제목", "faq_title")
        		)
        
    );

    private final String filterType;
    private final boolean showDatePicker;
    private final boolean showSelector;
    private final boolean showSearchText;
    private final boolean enableFilter;

    private final String filteringDateName;     // 👉 form에서 사용되는 name값
    private final String dateColumnName;   // 👉 실제 DB 컬럼명

    private final List<Option> selectOptions;

    @Getter
    @RequiredArgsConstructor
    public static class Option {
        private final String value;
        private final String label;
        private final String columnName;
    }

    public String resolveColumnName(String searchType) {
        return selectOptions.stream()
            .filter(opt -> opt.getValue().equals(searchType))
            .map(Option::getColumnName)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid searchType: " + searchType));
    }

    public static FilterConfig fromKey(String key) {
        return List.of(values()).stream()
            .filter(cfg -> cfg.getFilterType().equalsIgnoreCase(key))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid filter config key: " + key));
    }
    
    public String getFilterType() {
        return this.filterType;
    }
}


