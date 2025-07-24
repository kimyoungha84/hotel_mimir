package kr.co.sist.util;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
public class SortConditionBuilder {

    public SortParam build(MultiValueMap<String, String> params, FilterConfig config) {
        FilterConfig.SortSelectorOption sortOption = config.getSortSelectorOption();
        if (sortOption == null) {
            return null;
        }
        String sortValue = params.getFirst(sortOption.getSelectorName());
        if (sortValue == null) {
            return null;
        }
        return sortOption.getOptions().stream()
                .filter(opt -> opt.getValue().equals(sortValue))
                .findFirst()
                .map(opt -> new SortParam(opt.getColumnName(), opt.getDirection()))
                .orElse(null);
    }

    @Getter
    @RequiredArgsConstructor
    public static class SortParam {
        private final String columnName;
        private final String direction;
    }
} 