package kr.co.sist.admin.resvroom;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 인기 객실 (예약 순) 데이터를 담는 DTO
 */
@Getter
@Setter
@ToString
public class PopularRoomDTO {
    private String roomType; // 예: "디럭스", "스탠다드"
    private int reservationCount; // 예약 수
}