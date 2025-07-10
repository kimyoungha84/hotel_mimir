package kr.co.sist.room;


import java.util.List;

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
	private String description; 
	private List<String> imagePaths;
	private String thumbnailImage;
	private String status;
	private String floorType;
	
}//class
