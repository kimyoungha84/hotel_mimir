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
        ),
        List.of(  // 라벨 + 셀렉터 필터링 항목
        		new LabelSelectorOption("타입", "selectorType", "type",
        				List.of(
        						new LabelSelectorItem("전체", null), 
        						new LabelSelectorItem("한식", "한식"), 
        						new LabelSelectorItem("프렌치", "프렌치"), 
        						new LabelSelectorItem("레스토랑&바", "레스토랑&바"))
        				),
        		new LabelSelectorOption("위치", "selectorLocation", "location",
        				List.of(
        						
        						new LabelSelectorItem("전체", null), 
        						new LabelSelectorItem("1", "주소1"), 
        						new LabelSelectorItem("2", "주소2"), 
        						new LabelSelectorItem("3", "주소3"))
        				)
        		)
    ),

    FAQ(
            "faq", true, true, true, true,
            "faqRegDate",
            "faq_date",
            List.of(
                new Option("title", "제목", "faq_title")
            ),
            null
        ),
	
	 STAFF(
	            "staff", false, true, true, true,
	            null,
	            null,
	            List.of(
	                new Option("id", "아이디", "staff_id")
	            ),
	            List.of(  // 라벨 + 셀렉터 필터링 항목
	                new LabelSelectorOption("부서", "selectorDept", "dept_iden",
	                		List.of(new LabelSelectorItem("전체", null), 
	                				new LabelSelectorItem("객실", "room"), 
	                				new LabelSelectorItem("다이닝", "dinning"),
	                				new LabelSelectorItem("문의", "inquiry"),
	                				new LabelSelectorItem("인사", "person"),
	            					new LabelSelectorItem("멤버쉽", "member"))
	                ),
	                new LabelSelectorOption("직책", "selectorPosition", "position_identified_code",
	                		List.of(new LabelSelectorItem("전체", null), 
	                				new LabelSelectorItem("A", "A"), 
	                				new LabelSelectorItem("B", "B"),
	                				new LabelSelectorItem("C", "C"),
	                				new LabelSelectorItem("D", "D"),
	            					new LabelSelectorItem("E", "E"))
	                ),
	                new LabelSelectorOption("권한", "selectorPermission", "permission_id_code",
	                		List.of(new LabelSelectorItem("전체", null), 
	                				new LabelSelectorItem("객실", "room"), 
	                				new LabelSelectorItem("다이닝", "dinning"),
			                		new LabelSelectorItem("일반", "common"),
			                        new LabelSelectorItem("직원", "employee"),
			                        new LabelSelectorItem("어드민", "admin"))
	                ),
	                new LabelSelectorOption("상태", "selectorStatus", "staff_status",
	                		List.of(new LabelSelectorItem("전체", null), 
	                				new LabelSelectorItem("활성", "ACTIVE"), 
	                				new LabelSelectorItem("비활성", "DEACTIVE"))
	                )
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
    private final List<LabelSelectorOption> labelSelectorOptions;  // 추가된 부분

    @Getter
    @RequiredArgsConstructor
    public static class Option {
        private final String value;
        private final String label;
        private final String columnName;
    }
    
    @Getter
    @RequiredArgsConstructor
    public static class LabelSelectorOption {  // 라벨+셀렉터 필터링 옵션
        private final String label;  // 라벨 이름
        private final String selectorName;  // 셀렉터의 값 (각 셀렉터 필드에 대응되는 이름)
        private final String selColumnName;
        private final List<LabelSelectorItem> options;  // 선택 가능한 값들
    }

    @Getter
    @RequiredArgsConstructor
    public static class LabelSelectorItem {  // 셀렉터 항목
        private final String label;  // 셀렉터 항목에 표시될 라벨
        private final String searchValue;  // 선택된 값이 연결되는 컬럼명 (ex. "status_all", "category_tech")
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


