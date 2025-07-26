package kr.co.sist.dining.repmenu;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.dining.user.RepMenuDTO;
import kr.co.sist.dining.user.RepMenuDomain;

@Service
@Transactional
public class RepMenuService {
    
    @Autowired
    private RepMenuMapper repMenuMapper;
    
    public List<RepMenuDomain> getRepMenuListByDiningId(int diningId) {
        return repMenuMapper.selectRepMenuListByDiningId(diningId);
    }
    
    public RepMenuDomain getRepMenuByMenuId(int menuId) {
        return repMenuMapper.selectRepMenuByMenuId(menuId);
    }
    
    public boolean addRepMenu(RepMenuDTO repMenuDTO) {
        try {
            int result = repMenuMapper.insertRepMenu(repMenuDTO);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRepMenu(RepMenuDTO repMenuDTO) {
        try {
            int result = repMenuMapper.updateRepMenu(repMenuDTO);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRepMenu(int menuId) {
        try {
            int result = repMenuMapper.deleteRepMenu(menuId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 특정 다이닝의 고유한 분류 목록 조회
    public List<String> getDistinctDescriptionsByDiningId(int diningId) {
        return repMenuMapper.selectDistinctDescriptionsByDiningId(diningId);
    }
} 