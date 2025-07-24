package kr.co.sist.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum FilterConfig {

    DINING(
        "dining", false, true, true, true,
        "diningRegDate",     // 👉 form name
        "dining_reg_date",   // 👉 실제 컬럼명
        List.of(
            new Option("name", "이름", "dining_name")
        ),
        null,
        null,
        null
    ),

    DINING_USER("dining_user", false, true, true, true,
        null,
        null,
        List.of(new Option("name", "", "dining_name")),
        null,
        List.of(
            new LabelSelectorOption("키워드", "selectorType", "type",
                List.of(
                    new LabelSelectorItem("한식", "한식"),
                    new LabelSelectorItem("프렌치", "프렌치"),
                    new LabelSelectorItem("회원제 레스토랑", "회원제 레스토랑"),
                    new LabelSelectorItem("레스토랑 & 바", "레스토랑 & 바"),
                    new LabelSelectorItem("스카이 바", "스카이 바"),
                    new LabelSelectorItem("프리미엄 베이커리 & 카페", "프리미엄 베이커리 & 카페"),
                    new LabelSelectorItem("인룸 다이닝", "인룸 다이닝")
                )
            )
        ),
        null
    ),

    ROOM_USER("dining_user", false, false, true, true,
        null,
        null,
        List.of(new Option("name", "", "dining_name")),
        null,
        List.of(
            new LabelSelectorOption("키워드", "selectorType", "type",
                List.of(
                    new LabelSelectorItem("한식", "한식"),
                    new LabelSelectorItem("프렌치", "프렌치"),
                    new LabelSelectorItem("회원제 레스토랑", "회원제 레스토랑"),
                    new LabelSelectorItem("레스토랑&바", "레스토랑&바"),
                    new LabelSelectorItem("스카이 바", "스카이 바"),
                    new LabelSelectorItem("프리미엄 베이커리&카페", "프리미엄 베이커리&카페"),
                    new LabelSelectorItem("인룸 다이닝", "인룸 다이닝")
                )
            )
        ),
        null
    ),

    DINING_RESV("dining_resv", true, true, true, true,
        "reservationDate",
        "reservation_date",
        List.of(new Option("name", "이름", "dining_name")),
        null,
        null,
        null
    ),

    FAQ(
        "faq", true, true, true, true,
        "faqRegDate",
        "faq_date",
        List.of(
            new Option("title", "제목", "faq_title")
        ),
        null,
        null,
        null
    ),

    ROOM_LIST(
        "room_list", false, false, false, true,
        null,
        null,
        null,
        new SortSelectorOption(
            "정렬기준", "selectorOrder",
            List.of(
                new SortSelectorItem("낮은 요금순", "price-asc", "pricePerNight", "ASC"),
                new SortSelectorItem("높은 요금순", "price-desc", "pricePerNight", "DESC")
            )
        ),
        List.of(
            new LabelSelectorOption("침대 타입", "selectorBedName", "bed_name",
                List.of(
                    new LabelSelectorItem("슈퍼 킹 베드", "슈퍼 킹 베드"),
                    new LabelSelectorItem("킹 베드", "킹 베드"),
                    new LabelSelectorItem("트윈", "트윈"),
                    new LabelSelectorItem("더블", "더블")
                )
            ),
            new LabelSelectorOption("전망타입", "selectorViewName", "view_name",
                List.of(
                    new LabelSelectorItem("시티(전망욕실)", "시티(전망욕실)"),
                    new LabelSelectorItem("시티뷰", "시티뷰"),
                    new LabelSelectorItem("리버뷰", "리버뷰")
                )
            )
        ),
        null
    ),

    ROOM_RESV(
        "admin_room_resv", true, true, true, true,
        "resvRegDate",
        "resv_reg_date",
        List.of(
            new Option("searchUser", "", "user_name")
        ),
        null,
        List.of(
            new LabelSelectorOption("룸 종류", "roomType", "type_name",
                List.of(
                    new LabelSelectorItem("전체", null),
                    new LabelSelectorItem("로얄 스위트룸", "로얄 스위트룸"),
                    new LabelSelectorItem("프레지덴셜 스위트룸", "프레지덴셜 스위트룸"),
                    new LabelSelectorItem("스위트룸", "스위트룸"),
                    new LabelSelectorItem("프리미어룸", "프리미어룸"),
                    new LabelSelectorItem("스탠다드룸", "스탠다드룸")
                )
            ),
            new LabelSelectorOption("회원", "memberType", "ismember",
                List.of(
                    new LabelSelectorItem("전체", null),
                    new LabelSelectorItem("회원", "회원"),
                    new LabelSelectorItem("비회원", "비회원")
                )
            ),
            new LabelSelectorOption("예약 상태", "resvStatus", "status",
                List.of(
                    new LabelSelectorItem("전체", null),
                    new LabelSelectorItem("예약 완료", "예약완료"),
                    new LabelSelectorItem("예약 취소", "예약취소"),
                    new LabelSelectorItem("체크인", "체크인"),
                    new LabelSelectorItem("체크아웃", "체크아웃")
                )
            )
        ),
        null
    ),

    ROOM_SALES(
        "room_sales", true, true, false, true,
        "resvRegDate",
        "resv_reg_date",
        null,
        null,
        List.of(
            new LabelSelectorOption("룸 종류", "roomType", "type_name",
                List.of(
                    new LabelSelectorItem("로얄 스위트룸", "로얄 스위트룸"),
                    new LabelSelectorItem("프레지덴셜 스위트룸", "프레지덴셜 스위트룸"),
                    new LabelSelectorItem("스위트룸", "스위트룸"),
                    new LabelSelectorItem("프리미어룸", "프리미어룸"),
                    new LabelSelectorItem("스탠다드룸", "스탠다드룸")
                )
            )
        ),
        null
    ),

    STAFF(
        "staff", false, true, true, true,
        null,
        null,
        List.of(
            new Option("id", "아이디", "s.staff_id")
        ),
        null,
        List.of(
            new LabelSelectorOption("부서", "selectorDept", "dept_iden",
                List.of(
                    new LabelSelectorItem("전체", null),
                    new LabelSelectorItem("객실관리", "room"),
                    new LabelSelectorItem("다이닝 관리", "dinning"),
                    new LabelSelectorItem("문의 관리", "inquiry"),
                    new LabelSelectorItem("인사 관리", "person"),
                    new LabelSelectorItem("회원 관리", "member"),
                    new LabelSelectorItem("경영지원", "member")
                )
            ),
            new LabelSelectorOption("직책", "selectorPosition", "position_identified_code",
                List.of(
                    new LabelSelectorItem("전체", null),
                    new LabelSelectorItem("대표", "A"),
                    new LabelSelectorItem("팀장", "B"),
                    new LabelSelectorItem("과장", "C"),
                    new LabelSelectorItem("대리", "D"),
                    new LabelSelectorItem("사원", "E")
                )
            ),
            new LabelSelectorOption("권한", "selectorPermission", "permission_id_code",
                List.of(
                    new LabelSelectorItem("전체", null),
                    new LabelSelectorItem("객실", "room"),
                    new LabelSelectorItem("다이닝", "dinning"),
                    new LabelSelectorItem("문의", "inquiry"),
                    new LabelSelectorItem("회원", "member"),
                    new LabelSelectorItem("직원", "employee"),
                    new LabelSelectorItem("관리자", "admin")
                )
            ),
            new LabelSelectorOption("상태", "selectorStatus", "staff_status",
                List.of(
                    new LabelSelectorItem("전체", null),
                    new LabelSelectorItem("활성", "ACTIVE"),
                    new LabelSelectorItem("비활성", "DEACTIVE")
                )
            )
        ),
        null
    ),
    MEMBER(
        "member", false, true, true, true,
        null,
        null,
        List.of(
            new Option("userId", "아이디", "user_id"),
            new Option("userName", "이름", "user_name")
        ),
        null,
        null,
        List.of(new CheckboxOption("accUse","탈퇴","use_yn","N","Y"))
    );

    private final String filterType;
    private final boolean showDatePicker;
    private final boolean showSelector;
    private final boolean showSearchText;
    private final boolean enableFilter;
    private final String filteringDateName;
    private final String dateColumnName;
    private final List<Option> selectOptions;
    private final SortSelectorOption sortSelectorOption;
    private final List<LabelSelectorOption> labelSelectorOptions;
    private final List<CheckboxOption> checkboxOptions;

    
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

	@Getter
	@RequiredArgsConstructor
	public static class CheckboxOption {
		private final String name;         // form name (파라미터명)
		private final String label;        // 체크박스 앞 라벨
		private final String columnName;   // DB에서 비교할 컬럼명 ← 추가!
		private final String checkedValue; // 체크됐을 때 DB 값
		private final String uncheckedValue; // 체크 안됐을 때 DB 값
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

    @Getter
    @RequiredArgsConstructor
    public static class SortSelectorOption {
        private final String label; // ex) "정렬기준"
        private final String selectorName; // ex) "selectorOrder"
        private final List<SortSelectorItem> options;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SortSelectorItem {
        private final String label;      // ex) "낮은 요금순"
        private final String value;      // ex) "asc" (프론트에서 선택값)
        private final String columnName; // ex) "price_per_night" (DB 컬럼명)
        private final String direction;  // ex) "ASC" or "DESC"
    }
}


