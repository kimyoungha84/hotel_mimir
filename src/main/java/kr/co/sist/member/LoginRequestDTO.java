package kr.co.sist.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
	
	@Email
	@NotBlank
	private String email_id;
	
	@NotBlank
	private String password;
}
