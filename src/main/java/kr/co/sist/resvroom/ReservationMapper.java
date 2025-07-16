package kr.co.sist.resvroom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
	
	List<ReservationDTO> selectAllResv();
    
    ReservationDTO selectOneReservation(int ReservationNum);
    
    void insertReservation(ReservationDTO rDTO);
    
    void updateReservation(ReservationDTO rDTO);
   
}
