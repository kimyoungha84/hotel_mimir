package kr.co.sist.resvroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReservationService {
	
	@Autowired
	private ReservationMapper rm;
	
	public void addNonMemberReservation(ReservationDTO rDTO){
		rm.insertNonMemberReservation(rDTO);
	}//addReservation
	
	public ReservationDTO searchReservationData(ReservationDTO rDTO){
		return rm.selectReservationData(rDTO);
	}//addReservation
	
	public void addMemberReservation(ReservationDTO rDTO){
		rm.insertMemberReservation(rDTO);
	}//addReservation
	
	public int searchReservationSeq() {
		return rm.selectReservationSeq();
	}
	
	public ReservationDTO loginUserData(int userNum) {
	    ReservationDTO rsvDTO = rm.loginUserData(userNum);
	    
	    if (rsvDTO != null) {
	        // 이메일 나누기
	        if (rsvDTO.getUserEmail() != null) {
	            String emailFull = rsvDTO.getUserEmail();
	            String[] emailParts = emailFull.split("@");
	            
	            String email = emailParts.length > 0 ? emailParts[0] : "";
	            String domain = emailParts.length > 1 ? "@" + emailParts[1] : "";
	    
	            rsvDTO.setUserEmail(email);
	            rsvDTO.setUserDomain(domain);
	        }

	        // 전화번호 포맷팅 (xxx-xxxx-xxxx)
	        if (rsvDTO.getUserTel() != null) {
	            String tel = rsvDTO.getUserTel().replaceAll("[^0-9]", ""); // 숫자만 추출
	            if (tel.length() == 11) {
	                // 010-1234-5678
	                tel = tel.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
	            } else if (tel.length() == 10) {
	                // 02-1234-5678 또는 031-123-4567 같은 지역번호
	                tel = tel.replaceFirst("(\\d{2,3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
	            }
	            rsvDTO.setUserTel(tel);
	        }
	    }
	    
	    return rsvDTO;
	}
	
	public Integer checkRoomAvailability(ReservationDTO rDTO) {
		return rm.checkRoomAvailability(rDTO);
	}
	
}//class
