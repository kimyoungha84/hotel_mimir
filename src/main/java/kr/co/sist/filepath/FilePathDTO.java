package kr.co.sist.filepath;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilePathDTO {

	private int property_id;
	private String path;
	private String target_type;
	private String target_number;
	private String img_name;

	
}//class
