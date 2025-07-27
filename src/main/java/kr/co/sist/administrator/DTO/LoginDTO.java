package kr.co.sist.administrator;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("loginDTO")
public class LoginDTO {
	private String id;
	private String pass;
}//class
