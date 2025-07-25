package kr.co.sist.admin.resvroom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 객실별 점유율 데이터를 담는 DTO
 */
@Getter
@Setter
@ToString
public class RoomOccupancyDTO {
    private String roomType; // 예: "디럭스", "스위트"
    private double occupancyRate; // 예: 78.5 (%)
}