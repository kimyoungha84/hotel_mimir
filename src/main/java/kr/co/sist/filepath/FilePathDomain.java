package kr.co.sist.filepath;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("fileDomain")
@Getter
@Setter
@ToString
public class FilePathDomain {

	private int property_id;
	private String path;
	private String target_type;
	private String target_number;
	private String img_name;

	
}//class
