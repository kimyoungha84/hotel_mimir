package kr.co.sist.resvroom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
	
	public List<ReservationDTO> selectAllResv();
    
    public ReservationDTO selectOneReservation(int ReservationNum);
    
    public void insertReservation(ReservationDTO rDTO);
    
    public void updateReservation(ReservationDTO rDTO);
   
}
