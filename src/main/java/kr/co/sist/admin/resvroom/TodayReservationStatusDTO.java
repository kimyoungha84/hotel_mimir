package kr.co.sist.admin.resvroom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 오늘의 체크인/체크아웃 수 요약
 */
@Getter
@Setter
@ToString
public class TodayReservationStatusDTO {
    private int checkinCount;
    private int checkoutCount;
}