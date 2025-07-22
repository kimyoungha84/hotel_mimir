package kr.co.sist.nonmember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.diningresv.DiningResvDTO;
import kr.co.sist.resvroom.ReservationDTO;

@Service
public class NonMemberService {
	
	@Autowired
	private NonMemberMapper nmm;
	
	public void addNonMember(ReservationDTO rDTO){
		rDTO.setUserEmail(rDTO.getUserEmail()+rDTO.getUserDomain());
		nmm.insertNonMember(rDTO);
	}//addNonMember
	
	public int searchNonMemberSeq() {
		return nmm.selectNonMemberSeq();
	}
	
    public void insertNonMember2(DiningResvDTO dto) {
        nmm.insertNonMember2(dto);
    }

}//class
