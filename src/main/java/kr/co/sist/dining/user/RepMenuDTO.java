package kr.co.sist.dining.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RepMenuDTO {

	private int menuId;
	private int diningId;
	private String menuName;
	private String description;
	private int price;
}
