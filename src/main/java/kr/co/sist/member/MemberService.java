package kr.co.sist.member;

import java.util.List;
import java.util.Map;

import kr.co.sist.diningresv.DiningResvDTO;

public interface MemberService {
	
	LoginResponseDTO login(LoginRequestDTO loginDTO);

	boolean registerMember(MemberDTO mDTO);
	
	void updateRefreshToken(String emailId, String refreshToken);
	
    Map<String, String> sendAuthCodeWithJwt(String email);
    
    boolean verifyAuthCodeWithJwt(String token, String email, String code);

    void invalidateRefreshToken(String emailId); // 로그아웃 시 Refresh Token 무효화

    void updateLastLoginTime(String emailId);

    boolean isEmailDuplicated(String email);

    MemberDTO getMemberByEmail(String email);

    boolean checkPassword(String email, String password);

    int getExpectedRoomResvCount(String userNum);
    
    int getExpectedDiningResvCount(String userNum);

    boolean isUserExist(String name, String email);

    boolean updateProfile(MemberDTO memberDTO);

    boolean isUserExistByIdAndNameAndEmail(String userId, String name, String email);

    boolean resetPassword(String email, String newPassword);

    boolean withdrawMember(String email);
    
    List<RoomReservationDTO> getRoomReservationsByUserNum(String userNum);

    RoomReservationDTO getRoomReservationDetail(int reservationId);

    boolean cancelRoomReservation(int reservationId);
    
    List<DiningResvDTO> getDiningReservationsByUserNum(String userNum);

    DiningResvDTO getDiningReservationDetail(int reservationId);

    boolean cancelDiningReservation(int reservationId);
    
}
