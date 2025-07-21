package kr.co.sist.member;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sist.util.LoginJwtUtil;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private MemberMapper memberMapper;
	
    
    @Autowired
    private LoginJwtUtil loginJwtUtil;
    
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    
   
   
    
    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
    	//이메일로 회원조회
    	MemberDTO mDTO = memberMapper.selectMemberByEmail(loginDTO.getEmail_id());
    	
    	if(mDTO == null || !"N".equals(mDTO.getUse_yn())) {
    		throw new RuntimeException("존재하지 않거나 탈퇴한 회원입니다.");
    	}
    	
    	//비밀번호 검증
    	boolean matches = passwordEncoder.matches(loginDTO.getPassword(), mDTO.getPassword());
    	
    	if(!matches) {
    		throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    	}
    	
    	// 토큰발급
    	String accessToken = loginJwtUtil.generateAccessToken(mDTO.getUser_num(), mDTO.getEmail_id(), mDTO.getUser_name());
    	String refreshToken = loginJwtUtil.generateRefreshToken(mDTO.getUser_num());
    	
    	// 응답 반환
    	return new LoginResponseDTO(accessToken, refreshToken, mDTO.getUser_num(), mDTO.getEmail_id(), mDTO.getUser_name());
    }

    @Override
    public void updateRefreshToken(String emailId, String refreshToken) {
        Map<String, String> params = Map.of("emailId", emailId, "refreshToken", refreshToken);
        memberMapper.updateRefreshToken(params);
    }
    
    
    
    @Override
    public boolean registerMember(MemberDTO mDTO) {
    	
    	mDTO.setLogin_type("email");
    	mDTO.setReg_time(new Timestamp(System.currentTimeMillis()));
    	mDTO.setUse_yn("N");
    	
    	mDTO.setPassword(passwordEncoder.encode(mDTO.getPassword()));
    	
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
        String token = loginJwtUtil.generateEmailAuthToken(email, authCode, 300000); // 5분
        return Map.of("token", token);
    }

    @Override
    public boolean verifyAuthCodeWithJwt(String token, String email, String code) {
        return loginJwtUtil.validateEmailAuth(token, email, code);
    }

    @Override
    public void invalidateRefreshToken(String emailId) {
        memberMapper.invalidateRefreshToken(emailId);
    }

    @Override
    public void updateLastLoginTime(String emailId) {
        memberMapper.updateLastLoginTime(emailId);
    }

	@Override
	public boolean isEmailDuplicated(String email) {
		return memberMapper.selectMemberByEmail(email) != null;
	}

	@Override
	public MemberDTO getMemberByEmail(String email) {
		return memberMapper.selectMemberByEmail(email);
	}
    
}
