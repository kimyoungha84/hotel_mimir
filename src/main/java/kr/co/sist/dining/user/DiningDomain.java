package kr.co.sist.dining.user;


import java.util.Date;
import java.util.List;

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
	private String manager_name;
	private String dining_operation_hours;
	private Date dining_reg_date;
	private String dining_classification;
	private String dining_resv_availability;
	private String dining_detailinfo;
	private String dining_introduction;
	private String dining_main_image;
	private String dining_logo_image;
	private List<String> dining_carousel_images;
}
