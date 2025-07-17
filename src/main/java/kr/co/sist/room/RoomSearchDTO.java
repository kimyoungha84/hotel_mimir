package kr.co.sist.room;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomSearchDTO {
	
	private String bedName;
	private String typeName;
	private String viewName;
	private String checkIn;
    private String checkOut;

}