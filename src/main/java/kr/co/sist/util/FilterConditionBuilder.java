package kr.co.sist.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import kr.co.sist.util.FilterConfig.LabelSelectorItem;
import kr.co.sist.util.FilterConfig.LabelSelectorOption;

@Component
public class FilterConditionBuilder {
    
    private static final Logger logger = LoggerFactory.getLogger(FilterConditionBuilder.class);
    
    // 허용된 컬럼명들 (SQL Injection 방지)
    private static final Set<String> ALLOWED_COLUMNS = Set.of(
        // 다이닝 관련 컬럼
        "dining_id", "dining_name", "type", "location", "dining_reg_date",
        //다이닝 예약 관련 컬럼
        "reservation_id", "reservation_name", "reservation_date", "reservation_status",
        // FAQ 관련 컬럼  
        "faq_title", "faq_content", "faq_date",
        // 직원 관련 컬럼
        "s.staff_id", "staff_name", "dept_iden", "position_identified_code", 
        "permission_id_code", "staff_status",
        // Room 예약 관련 컬럼
        "user_name","type_name","ismember","status","resv_reg_date",
        // Member 관련 컬럼
        "use_yn","user_id","bed_name","view_name"
        
    );

    // 다중 선택 필터용 (MultiValueMap)
    public List<FilterCondition> build(MultiValueMap<String, String> params, FilterConfig config) {
        List<FilterCondition> list = new ArrayList<>();
        try {
            // 텍스트 검색 조건
            String searchType = params.getFirst("searchType");
            String keyword = params.getFirst("searchKeyword");
            if (isNotBlank(searchType) && isNotBlank(keyword)) {
                String column = config.resolveColumnName(searchType);
                if (isValidColumn(column)) {
                    list.add(new FilterCondition(column, FilterOperator.LIKE, keyword));
                    logger.debug("텍스트 검색 조건 추가 - column: {}, keyword: {}", column, keyword);
                } else {
                    logger.warn("허용되지 않은 컬럼명: {}", column);
                }
            }
            // 날짜 검색 조건
            String start = params.getFirst("startDate");
            String end = params.getFirst("endDate");
            String dateNameParam = params.getFirst("dateName");
            if (isNotBlank(dateNameParam) && dateNameParam.equals(config.getFilteringDateName())) {
                String column = config.getDateColumnName();
                if (isValidColumn(column)) {
                    if (isNotBlank(start) && isNotBlank(end)) {
                        list.add(new FilterCondition(column, FilterOperator.TRUNC_BETWEEN, List.of(start, end)));
                        logger.debug("날짜 범위 검색 조건 추가 - column: {}, start: {}, end: {}", column, start, end);
                    } else if (isNotBlank(start)) {
                        list.add(new FilterCondition(column, FilterOperator.TRUNC_GREATER_EQUAL, start));
                        logger.debug("시작일 검색 조건 추가 - column: {}, start: {}", column, start);
                    } else if (isNotBlank(end)) {
                        list.add(new FilterCondition(column, FilterOperator.TRUNC_LESS_EQUAL, end));
                        logger.debug("종료일 검색 조건 추가 - column: {}, end: {}", column, end);
                    }
                } else {
                    logger.warn("허용되지 않은 날짜 컬럼명: {}", column);
                }
            }
            // 라벨+셀렉터 필터링 조건 (다중 선택 지원)
            List<FilterConfig.LabelSelectorOption> selectorOptions = config.getLabelSelectorOptions();
            if (selectorOptions != null) {
                for (FilterConfig.LabelSelectorOption labelSelector : selectorOptions) {
                    List<String> selectedValues = params.get(labelSelector.getSelectorName());
                    if (selectedValues != null && !selectedValues.isEmpty()) {
                        String column = labelSelector.getSelColumnName();
                        if (isValidColumn(column)) {
                            // 여러 개 선택 시 IN, 한 개면 EQ
                            if (selectedValues.size() == 1) {
                                String selectedValue = selectedValues.get(0);
                                for (FilterConfig.LabelSelectorItem item : labelSelector.getOptions()) {
                                    if (item.getLabel().equals(selectedValue)) {
                                        if (item.getSearchValue() != null) {
                                            list.add(new FilterCondition(column, FilterOperator.EQ, item.getSearchValue()));
                                            logger.debug("셀렉터 검색 조건 추가 - column: {}, value: {}", column, item.getSearchValue());
                                        }
                                        break;
                                    }
                                }
                            } else {
                                // 여러 개 선택(IN)
                                List<String> inValues = new ArrayList<>();
                                for (String selectedValue : selectedValues) {
                                    for (FilterConfig.LabelSelectorItem item : labelSelector.getOptions()) {
                                        if (item.getLabel().equals(selectedValue) && item.getSearchValue() != null) {
                                            inValues.add(item.getSearchValue());
                                        }
                                    }
                                }
                                if (!inValues.isEmpty()) {
                                    list.add(new FilterCondition(column, FilterOperator.IN, inValues));
                                    logger.debug("셀렉터 IN 검색 조건 추가 - column: {}, values: {}", column, inValues);
                                }
                            }
                        } else {
                            logger.warn("허용되지 않은 셀렉터 컬럼명: {}", column);
                        }
                    }
                }
            }
            // 체크박스 필터링 조건 추가
            List<FilterConfig.CheckboxOption> checkboxOptions = config.getCheckboxOptions();
            if (checkboxOptions != null) {
                for (FilterConfig.CheckboxOption checkbox : checkboxOptions) {
                    List<String> values = params.get(checkbox.getName());
                    String checked = (values != null && !values.isEmpty()) ? values.get(values.size() - 1) : null;
                    if (checked == null) {
                        // 최초 호출(파라미터 없음) → 조건 추가 X
                        continue;
                    }
                    String column = checkbox.getColumnName();
                    if (isValidColumn(column)) {
                        if ("on".equals(checked)) {
                            list.add(new FilterCondition(column, FilterOperator.EQ, checkbox.getCheckedValue()));
                            logger.debug("체크박스(checked) 검색 조건 추가 - column: {}, value: {}", column, checkbox.getCheckedValue());
                        } else if ("off".equals(checked)) {
                            list.add(new FilterCondition(column, FilterOperator.EQ, checkbox.getUncheckedValue()));
                            logger.debug("체크박스(unchecked) 검색 조건 추가 - column: {}, value: {}", column, checkbox.getUncheckedValue());
                        }
                    }
                }
            }
            logger.debug("검색 조건 생성 완료 - 총 {}개 조건", list.size());
            return list;
        } catch (Exception e) {
            logger.error("검색 조건 생성 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    // 기존 단일값 Map 기반 기능은 그대로 유지
    public List<FilterCondition> build(Map<String, String> params, FilterConfig config) {
        List<FilterCondition> list = new ArrayList<>();
        try {
            // 텍스트 검색 조건
            String searchType = params.get("searchType");
            String keyword = params.get("searchKeyword");
            if (isNotBlank(searchType) && isNotBlank(keyword)) {
                String column = config.resolveColumnName(searchType);
                if (isValidColumn(column)) {
                    list.add(new FilterCondition(column, FilterOperator.LIKE, keyword));
                    logger.debug("텍스트 검색 조건 추가 - column: {}, keyword: {}", column, keyword);
                } else {
                    logger.warn("허용되지 않은 컬럼명: {}", column);
                }
            }
            // 날짜 검색 조건
            String start = params.get("startDate");
            String end = params.get("endDate");
            String dateNameParam = params.get("dateName");
            if (isNotBlank(dateNameParam) && dateNameParam.equals(config.getFilteringDateName())) {
                String column = config.getDateColumnName();
                if (isValidColumn(column)) {
                    if (isNotBlank(start) && isNotBlank(end)) {
                        list.add(new FilterCondition(column, FilterOperator.TRUNC_BETWEEN, List.of(start, end)));
                        logger.debug("날짜 범위 검색 조건 추가 - column: {}, start: {}, end: {}", column, start, end);
                    } else if (isNotBlank(start)) {
                        list.add(new FilterCondition(column, FilterOperator.TRUNC_GREATER_EQUAL, start));
                        logger.debug("시작일 검색 조건 추가 - column: {}, start: {}", column, start);
                    } else if (isNotBlank(end)) {
                        list.add(new FilterCondition(column, FilterOperator.TRUNC_LESS_EQUAL, end));
                        logger.debug("종료일 검색 조건 추가 - column: {}, end: {}", column, end);
                    }
                } else {
                    logger.warn("허용되지 않은 날짜 컬럼명: {}", column);
                }
            }
            // 라벨+셀렉터 필터링 조건 (단일값만 지원)
            List<FilterConfig.LabelSelectorOption> selectorOptions = config.getLabelSelectorOptions();
            if (selectorOptions != null) {
                for (FilterConfig.LabelSelectorOption labelSelector : selectorOptions) {
                    String selectedValue = params.get(labelSelector.getSelectorName());
                    if (isNotBlank(selectedValue)) {
                        String column = labelSelector.getSelColumnName();
                        if (isValidColumn(column)) {
                            for (FilterConfig.LabelSelectorItem item : labelSelector.getOptions()) {
                                if (item.getLabel().equals(selectedValue)) {
                                    if (item.getSearchValue() != null) {
                                        list.add(new FilterCondition(column, FilterOperator.EQ, item.getSearchValue()));
                                        logger.debug("셀렉터 검색 조건 추가 - column: {}, value: {}", column, item.getSearchValue());
                                    }
                                    break;
                                }
                            }
                        } else {
                            logger.warn("허용되지 않은 셀렉터 컬럼명: {}", column);
                        }
                    }
                }
            }
            // 체크박스 필터링 조건 추가 (단일값)
            List<FilterConfig.CheckboxOption> checkboxOptions = config.getCheckboxOptions();
            if (checkboxOptions != null) {
                for (FilterConfig.CheckboxOption checkbox : checkboxOptions) {
                    String checked = params.get(checkbox.getName());
                    if (checked == null) {
                        // 최초 호출(파라미터 없음) → 조건 추가 X
                        continue;
                    }
                    String column = checkbox.getColumnName();
                    if (isValidColumn(column)) {
                        if ("on".equals(checked)) {
                            list.add(new FilterCondition(column, FilterOperator.EQ, checkbox.getCheckedValue()));
                            logger.debug("체크박스(checked) 검색 조건 추가 - column: {}, value: {}", column, checkbox.getCheckedValue());
                        } else if ("off".equals(checked)) {
                            list.add(new FilterCondition(column, FilterOperator.EQ, checkbox.getUncheckedValue()));
                            logger.debug("체크박스(unchecked) 검색 조건 추가 - column: {}, value: {}", column, checkbox.getUncheckedValue());
                        }
                    }
                }
            }
            logger.debug("검색 조건 생성 완료 - 총 {}개 조건", list.size());
            return list;
        } catch (Exception e) {
            logger.error("검색 조건 생성 중 오류 발생", e);
            return new ArrayList<>();
        }
    }

    /**
     * 컬럼명이 허용된 목록에 있는지 검증
     */
    private boolean isValidColumn(String column) {
        if (column == null || column.trim().isEmpty()) {
            return false;
        }
        
        // SQL Injection 방지를 위한 추가 검증
        String normalizedColumn = column.trim().toLowerCase();
        
        // 허용된 컬럼명 목록에 있는지 확인
        boolean isValid = ALLOWED_COLUMNS.contains(normalizedColumn);
        
        // 추가 보안 검증: SQL 키워드나 특수문자 포함 여부 확인
        if (isValid) {
            String[] sqlKeywords = {"select", "insert", "update", "delete", "drop", "create", "alter", "union", "exec"};
            String[] dangerousChars = {"'", "\"", ";", "--", "/*", "*/", "xp_", "sp_"};
            
            for (String keyword : sqlKeywords) {
                if (normalizedColumn.contains(keyword)) {
                    logger.warn("SQL 키워드가 포함된 컬럼명 감지: {}", column);
                    return false;
                }
            }
            
            for (String dangerousChar : dangerousChars) {
                if (normalizedColumn.contains(dangerousChar)) {
                    logger.warn("위험한 문자가 포함된 컬럼명 감지: {}", column);
                    return false;
                }
            }
        }
        
        return isValid;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
