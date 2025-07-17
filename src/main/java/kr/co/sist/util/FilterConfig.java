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
        null
    ),
    
    
    DINING_USER("dining_user", false, true, true, true
    			, null
    			, null,
    			List.of(new Option("name", "", "dining_name")), 
    			
    			List.of(  // 라벨 + 셀렉터 필터링 항목
    		        new LabelSelectorOption("키워드", "selectorType", "type",
    		        		List.of(
    		        			new LabelSelectorItem("한식", "한식"), 
    		        			new LabelSelectorItem("프렌치", "프렌치"), 
    		        			new LabelSelectorItem("회원제 레스토랑", "회원제 레스토랑"), 
    		        			new LabelSelectorItem("레스토랑&바", "레스토랑&바"),
		    		        	new LabelSelectorItem("스카이 바", "스카이 바"),
    		        			new LabelSelectorItem("프리미엄 베이커리&카페", "프리미엄 베이커리&카페"),
    		        			new LabelSelectorItem("인룸 다이닝", "인룸 다이닝"))
    		        			),
    		        new LabelSelectorOption("위치", "selectorLocation", "location",
    		        		List.of(
    		        				new LabelSelectorItem("주소1", "주소1"), 
    		        				new LabelSelectorItem("주소2", "주소2"), 
    		        				new LabelSelectorItem("주소3", "주소3"), 
    		        				new LabelSelectorItem("주소4", "주소4"),
    		        				new LabelSelectorItem("주소5", "주소5"))
    		        		)
    		    )
    ),
    
    
    ROOM_USER("dining_user", false, false, true, true
			, null
			, null,
			List.of(new Option("name", "", "dining_name")), 
			
			List.of(  // 라벨 + 셀렉터 필터링 항목
		        new LabelSelectorOption("키워드", "selectorType", "type",
		        		List.of(
		        			new LabelSelectorItem("한식", "한식"), 
		        			new LabelSelectorItem("프렌치", "프렌치"), 
		        			new LabelSelectorItem("회원제 레스토랑", "회원제 레스토랑"), 
		        			new LabelSelectorItem("레스토랑&바", "레스토랑&바"),
	    		        	new LabelSelectorItem("스카이 바", "스카이 바"),
		        			new LabelSelectorItem("프리미엄 베이커리&카페", "프리미엄 베이커리&카페"),
		        			new LabelSelectorItem("인룸 다이닝", "인룸 다이닝"))
		        			)
		    )
    ),
    
    
    DINING_RESV("dining_resv", true, true, true, true,
    		"reservationDate",
    		"reservation_date",
    		List.of(new Option("name", "이름", "dining_name")),
    		null
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
    
    ROOM_LIST(
            "room_list", true, true, true, true,
            "",
            "",
            List.of(
                new Option("", "", "")
            ),
            List.of(  // 라벨 + 셀렉터 필터링 항목
    		        new LabelSelectorOption("", "정렬기준", "낮은요금&높은요금",
    		        		List.of(
    		        			new LabelSelectorItem("낮은 요금순", "asc"), 
    		        			new LabelSelectorItem("높은 요금순", "desc")) 
    		        			),
    		        new LabelSelectorOption("", "침대 타입", "bed_name",
    		        		List.of(
    		        			new LabelSelectorItem("슈퍼 킹 베드", "슈퍼 킹 베드"), 
    		        			new LabelSelectorItem("킹 베드", "킹 베드"), 
    		        			new LabelSelectorItem("트윈", "트윈"), 
    		        			new LabelSelectorItem("더블", "더블"))
    		        			),
    		        new LabelSelectorOption("", "전망타입", "view_name",
    		        		List.of(
    		        			new LabelSelectorItem("시티(전망욕실)", "예약 완료"), 
    		        			new LabelSelectorItem("시티뷰", "시티뷰"),
    		        			new LabelSelectorItem("리버뷰", "리버뷰"))
    		        			)
    		        
    		    )
        ),
    
    
    ROOM_RESV(
            "admin_room_resv", true, true, true, true,
            "resvRegDate",
            "resv_reg_date",
            List.of(
                new Option("searchUser", "", "user_num&non_mem_num부탁..")
            ),
            List.of(  // 라벨 + 셀렉터 필터링 항목
    		        new LabelSelectorOption("룸 종류", "roomType", "type_name",
    		        		List.of(
    		        			new LabelSelectorItem("로얄 스위트룸", "로얄 스위트룸"), 
    		        			new LabelSelectorItem("프레지덴셜 스위트룸", "프레지덴셜 스위트룸"), 
    		        			new LabelSelectorItem("스위트룸", "스위트룸"), 
    		        			new LabelSelectorItem("프리미어룸", "프리미어룸"),
    		        			new LabelSelectorItem("스탠다드룸", "스탠다드룸"))
    		        			),
    		        new LabelSelectorOption("회원", "memberType", "user_num&non_mem_num부탁..",
    		        		List.of(
    		        			new LabelSelectorItem("회원", "회원"), 
    		        			new LabelSelectorItem("비회원", "비회원"))
    		        			),
    		        new LabelSelectorOption("예약 상태", "resvStatus", "status",
    		        		List.of(
    		        			new LabelSelectorItem("예약 완료", "예약 완료"), 
    	    		        	new LabelSelectorItem("예약 취소", "예약 취소"),
    		        			new LabelSelectorItem("체크인 완료", "체크인 완료"),
    		        			new LabelSelectorItem("체크아웃 완료", "체크아웃 완료"))
    		        			)
    		        
    		    )
        ),
    
    
    ROOM_SALES(
            "admin_room_sales", true, true, true, true,
            "resvRegDate",
            "resv_reg_date",
            List.of(
                new Option("", "", "")
            ),
            List.of(
            		new LabelSelectorOption("룸 종류", "roomType", "type_name",
    		        		List.of(
    		        			new LabelSelectorItem("로얄 스위트룸", "로얄 스위트룸"), 
    		        			new LabelSelectorItem("프레지덴셜 스위트룸", "프레지덴셜 스위트룸"), 
    		        			new LabelSelectorItem("스위트룸", "스위트룸"), 
    		        			new LabelSelectorItem("프리미어룸", "프리미어룸"),
    		        			new LabelSelectorItem("스탠다드룸", "스탠다드룸"))
    		        			)
            		)
        ),
    
    
    
	
	 STAFF(
	            "staff", false, true, true, true,
	            null,
	            null,
	            List.of(
	                new Option("id", "아이디", "s.staff_id")
	            ),
	            List.of(  // 라벨 + 셀렉터 필터링 항목
	                new LabelSelectorOption("부서", "selectorDept", "dept_iden",
	                		List.of(new LabelSelectorItem("전체", null), 
	                				new LabelSelectorItem("객실관리", "room"), 
	                				new LabelSelectorItem("다이닝 관리", "dinning"),
	                				new LabelSelectorItem("문의 관리", "inquiry"),
	                				new LabelSelectorItem("인사 관리", "person"),
	            					new LabelSelectorItem("회원 관리", "member"),
	            					new LabelSelectorItem("경영지원", "member"))
	                ),
	                new LabelSelectorOption("직책", "selectorPosition", "position_identified_code",
	                		List.of(new LabelSelectorItem("전체", null), 
	                				new LabelSelectorItem("대표", "A"), 
	                				new LabelSelectorItem("팀장", "B"),
	                				new LabelSelectorItem("과장", "C"),
	                				new LabelSelectorItem("대리", "D"),
	            					new LabelSelectorItem("사원", "E"))
	                ),
	                new LabelSelectorOption("권한", "selectorPermission", "permission_id_code",
	                		List.of(new LabelSelectorItem("전체", null), 
	                				new LabelSelectorItem("객실", "room"), 
	                				new LabelSelectorItem("다이닝", "dinning"),
			                		new LabelSelectorItem("문의", "inquiry"),
			                        new LabelSelectorItem("회원", "member"),
			                        new LabelSelectorItem("직원", "employee"),
			                        new LabelSelectorItem("관리자", "admin"))
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


