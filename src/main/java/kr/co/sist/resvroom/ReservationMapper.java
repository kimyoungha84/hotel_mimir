package kr.co.sist.resvroom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
	
	public List<ReservationDTO> selectAllResv();
    
    public ReservationDTO selectOneResv(int resvId);
    
    public int selectReservationSeq();
    
    public Integer checkRoomAvailability(ReservationDTO rDTO);   
    
    public ReservationDTO loginUserData(int userNum);   
    
    public ReservationDTO selectReservationData(ReservationDTO rDTO);   
    
    public void insertNonMemberReservation(ReservationDTO rDTO);
    
    public void insertMemberReservation(ReservationDTO rDTO);
    
    public void updateReservation(ReservationDTO rDTO);
    
    public int updateResvAdmin(ReservationDTO rDTO);
    public int updateNonMemberAdmin(ReservationDTO rDTO);
    
}

