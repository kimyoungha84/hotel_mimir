package kr.co.sist.member.dto;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
	private int user_num;
	private String login_type;
	private String oauth_id;
	private String email_id;
	private String password;
	private String email_auth;
	private String email_refresh_token;
	private Timestamp email_auth_expire;
	@NotEmpty
	private String user_name;
	private Date birth_date;
	private String gender;
	@Pattern(regexp="^[0-9]{11}$")
	@NotEmpty
	private String tel;
	private Timestamp last_login_time;
	private String social_refresh_token;
	private Timestamp social_token_exprire;
	private Timestamp reg_time;
	private Timestamp update_time;
	private Timestamp quit_time;
	private String use_yn;
}
