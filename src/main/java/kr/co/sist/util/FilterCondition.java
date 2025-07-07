package kr.co.sist.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class FilterCondition {
    private String column;           // DB 컬럼명
    private FilterOperator operator;
    private Object value;
    

    // 생성자 등은 필요에 따라 추가
}
