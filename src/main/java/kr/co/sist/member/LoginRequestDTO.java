package kr.co.sist.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDTO {
	
	@Email
	@NotBlank
	private String email_id;
	
	@NotBlank
	private String password;
}
