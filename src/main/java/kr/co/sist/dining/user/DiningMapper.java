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
    
    List<RepMenuDomain> selectRepMenu(@Param("diningId") int diningId);
    
    void deleteDining(@Param("diningId") int diningId);
    void updateDining(DiningDTO dto);
    void deleteDiningImagesByType(@Param("diningId") int diningId, @Param("imageType") String imageType);
    void insertDiningImage(@Param("diningId") int diningId, @Param("imageType") String imageType, @Param("imageUrl") String imageUrl, @Param("displayOrder") int displayOrder);
    void deleteDiningImageByUrl(@Param("diningId") int diningId, @Param("imageUrl") String imageUrl, @Param("imageType") String imageType);
    void updateDisplayOrder(@Param("diningId") int diningId, @Param("imageUrl") String imageUrl, @Param("displayOrder") int displayOrder);
    void insertDining(DiningDTO dto);
    int selectLastInsertedDiningId();
    // 예약 가능(Y)인 다이닝 목록 조회
    List<DiningDomain> selectAvailableDiningList();
}
