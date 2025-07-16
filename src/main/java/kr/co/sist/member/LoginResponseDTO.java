package kr.co.sist.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
	
	private String accessToken;
	private String refreshToken;
	private int userNum;
	private String emailId;
	private String userName;
}
