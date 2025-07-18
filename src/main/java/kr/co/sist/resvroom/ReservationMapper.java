package kr.co.sist.resvroom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
	
	public List<ReservationDTO> selectAllResv();
    
    public ReservationDTO selectOneResv(int resvId);
    
    public int selectReservationSeq();
    
    public Integer checkRoomAvailability(ReservationDTO rDTO);   
    
    public void insertReservation(ReservationDTO rDTO);
    
    public void updateReservation(ReservationDTO rDTO);
    
}

