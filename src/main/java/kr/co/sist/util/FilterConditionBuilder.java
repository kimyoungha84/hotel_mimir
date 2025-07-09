package kr.co.sist.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import kr.co.sist.util.FilterConfig.LabelSelectorItem;
import kr.co.sist.util.FilterConfig.LabelSelectorOption;

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
        
        
        // Label + Selector 필터링 조건
        List<LabelSelectorOption> selectorOptions = config.getLabelSelectorOptions();
        if (selectorOptions != null) {
            for (LabelSelectorOption labelSelector : selectorOptions) {
                String selectedValue = params.get(labelSelector.getSelectorName());

                if (isNotBlank(selectedValue)) {
                    for (LabelSelectorItem item : labelSelector.getOptions()) {
                        if (item.getLabel().equals(selectedValue)) {
                        	if(item.getSearchValue() != null) {
                            list.add(new FilterCondition(labelSelector.getSelColumnName(), FilterOperator.EQ, item.getSearchValue()));
                        	}
                        }
                    }
                }
            }
        }



        return list;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
