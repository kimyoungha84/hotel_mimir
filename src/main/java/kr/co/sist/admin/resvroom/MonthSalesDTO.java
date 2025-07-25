package kr.co.sist.admin.resvroom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 월별 매출 데이터를 담는 DTO
 */
@Getter
@Setter
@ToString
public class MonthSalesDTO {
    private String month; // 예: "2025-07"
    private int salesAmount; // 총 매출액
}