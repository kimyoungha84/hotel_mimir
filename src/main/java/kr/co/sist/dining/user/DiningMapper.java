package kr.co.sist.dining.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DiningMapper {
    // 다이닝 기본 정보 + 메인/로고 이미지
    DiningDomain selectDiningInfoBase(@Param("diningId") int diningId);
    // 케러셀 이미지 리스트
    List<String> selectDiningCarouselImages(@Param("diningId") int diningId);
}
