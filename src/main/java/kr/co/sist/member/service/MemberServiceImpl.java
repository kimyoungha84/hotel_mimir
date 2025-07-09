package kr.co.sist.member.service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import kr.co.sist.member.dao.MemberMapper;
import kr.co.sist.member.dto.MemberDTO;
import kr.co.sist.util.JwtUtil;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Override
    public boolean registerMember(MemberDTO mDTO) {
    	
    	mDTO.setLogin_type("email");
    	mDTO.setReg_time(new Timestamp(System.currentTimeMillis()));
    	mDTO.setUse_yn("N");
    	
    	int result = memberMapper.insertMember(mDTO);
    	
    	return result == 1;
    }

    @Override
    public Map<String, String> sendAuthCodeWithJwt(String email) {
        String authCode = String.valueOf(new Random().nextInt(900000) + 100000);

        // 이메일 전송
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("[HOTEL MIMIR] 이메일 인증번호");
        msg.setText("인증번호는 [" + authCode + "] 입니다. 5분 이내에 입력해주세요.");
        mailSender.send(msg);

        // JWT 생성
        String token = jwtUtil.generateEmailAuthToken(email, authCode, 300000); // 5분
        return Map.of("token", token);
    }

    @Override
    public boolean verifyAuthCodeWithJwt(String token, String email, String code) {
        return jwtUtil.validateEmailAuth(token, email, code);
    }
	
}
