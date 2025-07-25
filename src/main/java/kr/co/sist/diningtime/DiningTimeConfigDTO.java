package kr.co.sist.diningtime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiningTimeConfigDTO {

    private int configId;
    private int diningId;
    private String mealType;
    private String timeSlot;
	
}
