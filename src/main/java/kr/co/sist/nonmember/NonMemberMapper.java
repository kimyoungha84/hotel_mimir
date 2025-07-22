package kr.co.sist.nonmember;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.resvroom.ReservationDTO;

@Mapper
public interface NonMemberMapper {
	public List<NonMemberDTO> selectAllNonMember();
    
    public NonMemberDTO selectOneNonMember(int nonMemNum);
    
    public int selectNonMemberSeq();
    
    public void insertNonMember(ReservationDTO rDTO);
    
    public void updateNonMember(NonMemberDTO nonDTO);
   
    public void insertNonMember2(DiningResvDTO dto);
}
