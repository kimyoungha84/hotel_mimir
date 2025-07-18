package kr.co.sist.diningresv;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.sist.dining.user.DiningDomain;

@Mapper
public interface DiningResvMapper {

	public List<DiningResvDTO> selectAllResv();
	
	DiningResvDTO selectResvId(@Param("reservationId") int reservationId);
	
	DiningDomain selectDiningId(int diningId);
	
	List<DiningDomain> selectAllDining();
	
	int updateResvInfo(DiningResvDTO dto);
	
}
