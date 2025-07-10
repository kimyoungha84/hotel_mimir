package kr.co.sist.room;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("bedTypeDomain")
@Getter
@Setter
@ToString
public class BedTypeDomain {
	private int bedId;
	private String bedName;
	
}//class
