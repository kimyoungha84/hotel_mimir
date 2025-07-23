package kr.co.sist.util.domain;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("SearchDataDomain")
@Getter
@Setter
@ToString
public class SearchDataDomain {
	
	//DINING
	private int dining_id;
	private String dining_name;
	private String type;
	private String manager_name;
	private Date dining_reg_date;
	private String location;
	private String phone_number;
	private String dining_classification;
	private String dining_main_image;
	
	//FAQ
	private int faq_num;
	private String faq_title;
	private String faq_content;
	private Date faq_date;
	
	//STAFF
	private String staff_id; 
	private String staff_name; 
	private String dept_iden; 
	private char position_identified_code; 
	private String permission_ids; 
	private String staff_status;
	
	//DINING_RESV
	private int reservation_id;
	private String reservation_name;
	private Date reservation_date;
	private String reservation_status;
	private String reservation_type;
	
	//ROOM_RESV
	private int resvId;
	private int roomId;
	private String typeName;
	private Boolean isMember;
	private String userName;
	private String status; 
	
	//ROOM_USER
	private String bedName;
	private String floorType;
	private String viewName;
	private int countAvailableRooms;
	
	//MEMBER
	private int user_num;
	private String login_type;
	private String user_id; // oauth_id/email_id 대신 user_id만 둠 (SQL에서 as user_id)
	private String user_name;
	private String birth_date;
	private String tel;
	private Timestamp reg_time;
	private char use_yn;
	
	
}//class