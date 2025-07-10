package kr.co.sist.room;


import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("roomDomain")
@Getter
@Setter
@ToString
public class RoomDomain {
	private int roomId;  
	private int bedId;
	private int typeId;
	private int viewId;
	private int capacity;
	private double pricePerNight;
	private double area_Sqm;
	private int roomFloor;
	private String description;
	
}//class
