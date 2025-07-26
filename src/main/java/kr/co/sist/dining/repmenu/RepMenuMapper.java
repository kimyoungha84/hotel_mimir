package kr.co.sist.dining.repmenu;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import kr.co.sist.dining.user.RepMenuDTO;
import kr.co.sist.dining.user.RepMenuDomain;

@Mapper
public interface RepMenuMapper {
    
    List<RepMenuDomain> selectRepMenuListByDiningId(int diningId);
    
    RepMenuDomain selectRepMenuByMenuId(int menuId);
    
    int insertRepMenu(RepMenuDTO repMenuDTO);
    
    int updateRepMenu(RepMenuDTO repMenuDTO);
    
    int deleteRepMenu(int menuId);
    
    // 특정 다이닝의 고유한 분류 목록 조회
    List<String> selectDistinctDescriptionsByDiningId(int diningId);
} 