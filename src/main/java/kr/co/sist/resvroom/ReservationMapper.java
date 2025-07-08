package kr.co.sist.resvroom;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.FAQ.FAQDTO;

@Mapper
public interface ReservationMapper {
    List<ReservationDTO> selectAllReservation();
    
    void insertReservation(ReservationDomain rDomain);
    
    ReservationDomain selectOneReservation(int ReservationNum);
    
    void updateFaq(ReservationDomain rDomain);
   
}
