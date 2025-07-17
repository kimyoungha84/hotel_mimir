package kr.co.sist.room;



import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("roomDTO")
@Getter
@Setter
@ToString
public class RoomDTO {

	private int roomId;  
	private String bedName; 
	private String typeName;
	private String viewName;
	private int capacity; 
	private double pricePerNight;
	private double area_Sqm;
	private int roomFloor; 
	private int countAvailableRooms; 
	private String description; 
	private String imagePath;
	private String status;
	private String floorType;
	
}//class
