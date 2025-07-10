package kr.co.sist.room;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("viewTypeDomain")
@Getter
@Setter
@ToString
public class ViewTypeDomain {
	private int viewId;
	private String viewName;
	
}//class
