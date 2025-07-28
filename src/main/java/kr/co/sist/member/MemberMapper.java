package kr.co.sist.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.diningresv.DiningResvDTO;

@Mapper
public interface MemberMapper {
		
	int insertMember(MemberDTO mDTO);
		
	int countByEmail(String email_id);
		
	MemberDTO selectMemberByEmail(String email_id);
		
	void updateRefreshToken(Map<String, String> params);

	MemberDTO selectMemberByUserNum(int userNum);

	void invalidateRefreshToken(String emailId);

	void updateLastLoginTime(String emailId);

	int selectExpectedRoomResvCount(String userNum);
	
	int selectExpectedDiningResvCount(String userNum);

    MemberDTO selectUserByNameAndEmail(Map<String, String> params);

    int updateProfile(MemberDTO memberDTO);

    MemberDTO selectUserByIdAndNameAndEmail(Map<String, String> params);

    int updatePassword(Map<String, String> params);

    int updateMemberToWithdrawn(String email);

    List<RoomReservationDTO> selectRoomReservationsByUserNum(String userNum);

    RoomReservationDTO selectRoomReservationDetail(int reservationId);

    int updateRoomReservationStatusToCancelled(int reservationId);
    
    List<DiningResvDTO> selectDiningReservationsByUserNum(String userNum);

    DiningResvDTO selectDiningReservationDetail(int reservationId);

    int updateDiningReservationStatusToCancelled(int reservationId);
    
    
}