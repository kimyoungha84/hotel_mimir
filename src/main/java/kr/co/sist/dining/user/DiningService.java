package kr.co.sist.dining.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiningService {

    @Autowired
    DiningMapper diningMapper;

    public DiningDomain searchOneDining(int diningId) {
        // 1. 기본 정보 + 메인/로고 이미지
        DiningDomain domain = diningMapper.selectDiningInfoBase(diningId);
        // 2. 케러셀 이미지 리스트
        List<String> carouselImages = diningMapper.selectDiningCarouselImages(diningId);
        domain.setDining_carousel_images(carouselImages);
        return domain;
    }
}
