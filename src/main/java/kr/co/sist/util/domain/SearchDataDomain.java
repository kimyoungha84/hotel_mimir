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
	private String dining_name;
	private int dining_price;
	private Date dining_reg_date;
	
	//FAQ
	private int faq_num;
	private String faq_title;
	private String faq_content;
	private Date faq_date;
	
	
}//class
