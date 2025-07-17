package kr.co.sist.dining.user;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiningDomain {
	private int dining_id;
	private String dining_name;
	private String type;
	private String location;
	private String phone_number;
	private String dining_classification;
	private char dining_resv_availability;
	private String dining_introduction;
}
