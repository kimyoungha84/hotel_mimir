package kr.co.sist.resvroom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
    List<JoinReservationDTO> selectAllReservation();
    
    JoinReservationDTO selectOneReservation(int ReservationNum);
    
    void insertReservation(ReservationDTO rDTO);
    
    void updateReservation(ReservationDTO rDTO);
   
}
