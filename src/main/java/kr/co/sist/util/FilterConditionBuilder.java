package kr.co.sist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class FilterConditionBuilder {

    public List<FilterCondition> build(Map<String, String> params, FilterConfig config) {
        List<FilterCondition> list = new ArrayList<>();

        String searchType = params.get("searchType");
        String keyword = params.get("searchKeyword");

        if (isNotBlank(searchType) && isNotBlank(keyword)) {
            try {
                String column = config.resolveColumnName(searchType);
                list.add(new FilterCondition(column, FilterOperator.LIKE, keyword));
            } catch (IllegalArgumentException e) {
                // 무시 또는 로깅
            }
        }

        String start = params.get("startDate");
        String end = params.get("endDate");
        String dateNameParam = params.get("dateName");

        if (isNotBlank(dateNameParam) && dateNameParam.equals(config.getFilteringDateName())) {
            String column = config.getDateColumnName();

            if (isNotBlank(start) && isNotBlank(end)) {
                list.add(new FilterCondition(column, FilterOperator.TRUNC_BETWEEN, List.of(start, end)));
            } else if (isNotBlank(start)) {
                list.add(new FilterCondition(column, FilterOperator.TRUNC_GREATER_EQUAL, start));
            } else if (isNotBlank(end)) {
                list.add(new FilterCondition(column, FilterOperator.TRUNC_LESS_EQUAL, end));
            }
        }

        return list;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
