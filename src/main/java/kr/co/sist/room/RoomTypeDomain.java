package kr.co.sist.room;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("roomTypeDomain")
@Getter
@Setter
@ToString
public class RoomTypeDomain {
	private int typeId;
	private String typeName;
}//class
