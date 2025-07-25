package kr.co.sist.admin.resvroom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 월별 예약 건수를 담는 DTO
 */
@Getter
@Setter
@ToString
public class MonthCountDTO {
    private String month; // 예: "2025-07"
    private int reservationCount;
}