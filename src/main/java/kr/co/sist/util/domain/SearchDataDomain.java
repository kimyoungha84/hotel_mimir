package kr.co.sist.util.domain;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("SearchDataDomain")
@Getter
@Setter
@ToString
public class SearchDataDomain {
	
	//다이닝
	private int dining_id;
	private String dining_name;
	private String type;
	private String manager_name;
	private Date dining_reg_date;
	
	//FAQ
	private int faq_num;
	private String faq_title;
	private String faq_content;
	private Date faq_date;
	
	
}//class
