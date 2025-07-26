package kr.co.sist.dining.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RepMenuDomain {
	
	private int menu_id;
	private String menu_name;
	private String description;
	private int price;
	
	
}
