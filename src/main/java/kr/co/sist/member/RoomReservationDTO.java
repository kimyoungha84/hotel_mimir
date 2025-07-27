package kr.co.sist.member;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomReservationDTO {
	private int user_num;
    private int reservation_id;
    private String room_name;
    private Date check_in_date;
    private Date check_out_date;
    private int num_guests_adult;     // 추가
    private int num_guests_child;     // 추가
    private String reservation_status;
    private String request_msg;       // 추가
    private int total_price;

}