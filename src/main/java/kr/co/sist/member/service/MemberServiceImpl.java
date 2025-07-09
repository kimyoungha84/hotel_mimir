package kr.co.sist.member.service;

import java.sql.Timestamp;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.member.dao.MemberMapper;
import kr.co.sist.member.dto.MemberDTO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public void sendAuthCode(String email, HttpSession session) {
		String authCode = String.valueOf(new Random().nextInt(900000) + 100000);

		// 이메일 발송
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("[HOTEL MIMIR] 회원가입 인증번호 안내");
		message.setText("인증번호는 [" + authCode + "] 입니다. 5분 이내에 입력해 주세요.");
		mailSender.send(message);

		// 세션 저장
		session.setAttribute("authEmail", email);
		session.setAttribute("authCode", authCode);
		session.setMaxInactiveInterval(300);

	}

	@Override
	public boolean verifyAuthCode(String email, String code, HttpSession session) {
		String savedEmail = (String) session.getAttribute("authEmail");
		String savedCode = (String) session.getAttribute("authCode");

		return email != null && code != null && email.equals(savedEmail) && code.equals(savedCode);
	}

	@Override
	public void registerMember(MemberDTO mDTO) {
		String encodedPw = new BCryptPasswordEncoder().encode(mDTO.getPassword());
		mDTO.setPassword(encodedPw);
		mDTO.setReg_time(new Timestamp(System.currentTimeMillis()));
		mDTO.setUse_yn("N");
		mDTO.setLogin_type("email");

		memberMapper.insertMember(mDTO);
	}
	
}
