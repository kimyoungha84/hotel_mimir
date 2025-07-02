package kr.co.sist.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class FAQDTO {

	private Integer faq_num;
	private String faq_title;
	private String faq_content;
	private Date faq_date;
}
