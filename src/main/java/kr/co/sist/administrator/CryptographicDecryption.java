package kr.co.sist.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CryptographicDecryption {
	@Autowired
	PasswordEncoder pe;
	
	/*Bcrypt를 이용해서, 일방향 해쉬 함수, 암호화*/
	public String useBcryptEncryption(String plainText) {
		String cipherText="";
		
		//1.passwordEncoder 생성
		pe=new BCryptPasswordEncoder();
		
		//2.일방향 해쉬생성
		cipherText=pe.encode(plainText);
		
		
		return cipherText;
	}//useBcryptEncryption
	
	
	public boolean useBcryptMatches(String plainText, String useBcryptValue) {
		
		boolean comparebooleanValue=pe.matches(plainText, useBcryptValue);
		
		return comparebooleanValue;
	}//useBcryptMatches
	
}//class
