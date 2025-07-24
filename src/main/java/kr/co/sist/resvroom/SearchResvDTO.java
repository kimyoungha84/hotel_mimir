package kr.co.sist.resvroom;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("sResvDTO")
@Getter
@Setter
@ToString
public class SearchResvDTO {

	private String typeName; 
	private String userType;
	private Date checkinDate;  
	private Date checkoutDate;  
	private String status;  
	private String resvName;
	
	
}//class
