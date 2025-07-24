package kr.co.sist.dining.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

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
    
    public List<RepMenuDomain> searchRepMenu(int diningId){
    	
    	
    	return diningMapper.selectRepMenu(diningId);
    	
    }

    @Transactional
    public boolean deleteDining(int diningId) {
        try {
            int affected = diningMapper.deleteDining(diningId);
            return affected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean updateDiningData(DiningDTO dto) {
        try {
            int affected = diningMapper.updateDining(dto);
            return affected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public String updateDiningFiles(int diningId, MultipartFile mainImage, MultipartFile logoImage, MultipartFile carouselImage, Integer carouselIndex) {
        String projectRoot = System.getProperty("user.dir");
        String basePath = projectRoot + "/src/main/resources/static/dining/images/";
        String uploadedUrl = null;
        try {
            // 메인 이미지
            if (mainImage != null && !mainImage.isEmpty()) {
                diningMapper.deleteDiningImagesByType(diningId, "MAIN");
                String originalFilename = mainImage.getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String mainFileName = "main_" + diningId + "_" + System.currentTimeMillis() + extension;
                File mainDir = new File(basePath + "main/");
                if (!mainDir.exists()) mainDir.mkdirs();
                File mainFile = new File(basePath + "main/" + mainFileName);
                mainImage.transferTo(mainFile);
                uploadedUrl = "/dining/images/main/" + mainFileName;
                diningMapper.insertDiningImage(diningId, "MAIN", uploadedUrl, 1);
            }
            // 로고 이미지
            if (logoImage != null && !logoImage.isEmpty()) {
                diningMapper.deleteDiningImagesByType(diningId, "LOGO");
                String originalFilename = logoImage.getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String logoFileName = "logo_" + diningId + "_" + System.currentTimeMillis() + extension;
                File logoDir = new File(basePath + "logo/");
                if (!logoDir.exists()) logoDir.mkdirs();
                File logoFile = new File(basePath + "logo/" + logoFileName);
                logoImage.transferTo(logoFile);
                uploadedUrl = "/dining/images/logo/" + logoFileName;
                diningMapper.insertDiningImage(diningId, "LOGO", uploadedUrl, 1);
            }
            // 케러셀 이미지 (단일, index)
            if (carouselImage != null && !carouselImage.isEmpty() && carouselIndex != null) {
                List<String> oldUrls = diningMapper.selectDiningCarouselImages(diningId);
                if (oldUrls.size() >= carouselIndex) {
                    String oldUrl = oldUrls.get(carouselIndex - 1);
                    diningMapper.deleteDiningImageByUrl(diningId, oldUrl, "CAROUSEL");
                    String filePath = projectRoot + "/src/main/resources/static" + oldUrl;
                    File file = new File(filePath);
                    if (file.exists()) file.delete();
                }
                String originalFilename = carouselImage.getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String carouselFileName = "carousel_" + diningId + "_" + System.currentTimeMillis() + "_" + carouselIndex + extension;
                File carouselDir = new File(basePath + "carousel/");
                if (!carouselDir.exists()) carouselDir.mkdirs();
                File carouselFile = new File(basePath + "carousel/" + carouselFileName);
                carouselImage.transferTo(carouselFile);
                uploadedUrl = "/dining/images/carousel/" + carouselFileName;
                diningMapper.insertDiningImage(diningId, "CAROUSEL", uploadedUrl, carouselIndex);
            }
            return uploadedUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean saveCarouselImages(int diningId, List<MultipartFile> files, List<String> urls, List<String> types) {
        try {
            // 1. 기존 케러셀 이미지 모두 삭제 (DB, 파일)
            List<String> oldUrls = diningMapper.selectDiningCarouselImages(diningId);
            for (String url : oldUrls) {
                diningMapper.deleteDiningImageByUrl(diningId, url, "CAROUSEL");
                String filePath = System.getProperty("user.dir") + "/src/main/resources/static" + url;
                File file = new File(filePath);
                if (file.exists()) file.delete();
            }
            // 2. 새 순서대로 insert (파일은 저장, URL은 그대로)
            int fileIdx = 0, urlIdx = 0;
            for (int i = 0; i < types.size(); i++) {
                if ("file".equals(types.get(i))) {
                    MultipartFile file = files.get(fileIdx++);
                    String originalFilename = file.getOriginalFilename();
                    String extension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }
                    String fileName = "carousel_" + diningId + "_" + System.currentTimeMillis() + "_" + (i+1) + extension;
                    String basePath = System.getProperty("user.dir") + "/src/main/resources/static/dining/images/carousel/";
                    File dir = new File(basePath);
                    if (!dir.exists()) dir.mkdirs();
                    File dest = new File(basePath + fileName);
                    file.transferTo(dest);
                    String url = "/dining/images/carousel/" + fileName;
                    diningMapper.insertDiningImage(diningId, "CAROUSEL", url, i+1);
                } else if ("url".equals(types.get(i))) {
                    String url = urls.get(urlIdx++);
                    diningMapper.insertDiningImage(diningId, "CAROUSEL", url, i+1);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean updateCarouselOrder(int diningId, List<String> imageUrls, List<String> deletedImageUrls) {
        try {
            // 삭제 먼저
            if (deletedImageUrls != null) {
                for (String url : deletedImageUrls) {
                    System.out.println("[Service] 삭제 시도: " + url);
                    diningMapper.deleteDiningImageByUrl(diningId, url, "CAROUSEL");
                    // 파일도 삭제
                    String filePath = System.getProperty("user.dir") + "/src/main/resources/static" + url;
                    File file = new File(filePath);
                    if (file.exists()) file.delete();
                }
            }
            // 순서 update
            for (int i = 0; i < imageUrls.size(); i++) {
                System.out.println("[Service] 순서 update: " + imageUrls.get(i) + " → " + (i+1));
                diningMapper.updateDisplayOrder(diningId, imageUrls.get(i), i + 1);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deleteDiningImage(int diningId, String imageUrl, String imageType) {
        try {
            // DB에서 삭제
            diningMapper.deleteDiningImageByUrl(diningId, imageUrl, imageType);
            // 파일 시스템에서 삭제
            String projectRoot = System.getProperty("user.dir");
            String filePath = projectRoot + "/src/main/resources/static" + imageUrl;
            File file = new File(filePath);
            if (file.exists()) file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean insertDining(DiningDTO dto) {
        try {
            int affected = diningMapper.insertDining(dto);
            return affected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public int insertDiningAndReturnId(DiningDTO dto) {
        diningMapper.insertDining(dto);
        // 시퀀스에서 방금 insert된 PK 반환 (Oracle 기준)
        return diningMapper.selectLastInsertedDiningId();
    }

    // 예약 가능(Y)인 다이닝 목록 조회
    public List<DiningDomain> getAvailableDiningList() {
        return diningMapper.selectAvailableDiningList();
    }
}
