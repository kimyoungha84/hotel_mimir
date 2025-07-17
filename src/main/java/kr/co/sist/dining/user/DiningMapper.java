package kr.co.sist.dining.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiningMapper {

	DiningDomain selectDiningInfo(int diningId);
		
}
