package kr.co.sist.diningresv;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DiningResvMapper {

	public List<DiningResvDTO> selectAllResv();
	
	DiningResvDTO selectResvId(@Param("reservationId") int reservationId);
	
	int updateResvInfo(DiningResvDTO dto);
	
}
