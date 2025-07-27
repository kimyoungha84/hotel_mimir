package kr.co.sist.member;

import java.sql.Timestamp;
import java.util.List;
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
    	
    	if(mDTO == null || !"Y".equals(mDTO.getUse_yn())) {
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
    	mDTO.setUse_yn("Y");
    	
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

	@Override
	public boolean checkPassword(String email, String password) {
		MemberDTO member = memberMapper.selectMemberByEmail(email);
		
		if (member == null) {
			return false; // 사용자가 존재하지 않음
		}
		
		// BCryptPasswordEncoder를 사용하여 비밀번호 일치 여부 확인
		return passwordEncoder.matches(password, member.getPassword());
	}

	@Override
    public int getExpectedRoomResvCount(String userNum) {
        return memberMapper.selectExpectedRoomResvCount(userNum);
    }

    @Override
    public boolean isUserExist(String name, String email) {
        Map<String, String> params = Map.of("name", name, "email", email);
        return memberMapper.selectUserByNameAndEmail(params) != null;
    }

    @Override
    public boolean updateProfile(MemberDTO memberDTO) {
        return memberMapper.updateProfile(memberDTO) == 1;
    }

    @Override
    public boolean isUserExistByIdAndNameAndEmail(String userId, String name, String email) {
        Map<String, String> params = Map.of("userId", userId, "name", name, "email", email);
        return memberMapper.selectUserByIdAndNameAndEmail(params) != null;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        Map<String, String> params = Map.of("email", email, "newPassword", encodedPassword);
        return memberMapper.updatePassword(params) == 1;
    }

    @Override
    public boolean withdrawMember(String email) {
        System.out.println("회원 탈퇴 서비스 호출 - 이메일: " + email);
        int result = memberMapper.updateMemberToWithdrawn(email);
        System.out.println("회원 탈퇴 매퍼 결과: " + result);
        return result == 1;
    }

    @Override
    public List<RoomReservationDTO> getRoomReservationsByUserNum(String userNum) {
        return memberMapper.selectRoomReservationsByUserNum(userNum);
    }

    @Override
    public RoomReservationDTO getRoomReservationDetail(int reservationId) {
        return memberMapper.selectRoomReservationDetail(reservationId);
    }

    @Override
    public boolean cancelRoomReservation(int reservationId) {
        return memberMapper.updateRoomReservationStatusToCancelled(reservationId) == 1;
    }
}