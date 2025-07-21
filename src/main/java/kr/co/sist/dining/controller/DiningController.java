package kr.co.sist.dining.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sist.dining.user.DiningDomain;
import kr.co.sist.dining.user.DiningService;
import kr.co.sist.dining.user.RepMenuDomain;
import kr.co.sist.util.FilterConfig;
import kr.co.sist.util.ModelUtils;
import kr.co.sist.util.controller.SearchController;
import kr.co.sist.dining.user.DiningDTO;

@Controller
public class DiningController {
	@Autowired
	ModelUtils modelUtils;
	@Autowired
	DiningService ds;
	
	@GetMapping("/admin/dining")
	public String adminDining( Model model ) {
		int pageSize = 5; // 페이지당 항목 수

		// fragment 정보 동적 등록
		SearchController.addFragmentInfo(
			FilterConfig.DINING,
			"admin_dining/admin_dining",
			"dining_list_fm",
			"diningList"
		);
		modelUtils.setFilteringInfo(model, FilterConfig.DINING);
		modelUtils.setPaginationAttributes(model, pageSize, FilterConfig.DINING);
		return "admin_dining/admin_dining";
	}
	
	@GetMapping("/user/dining_main")
	public String diningMain(Model model) {
		
		// fragment 정보 동적 등록
				SearchController.addFragmentInfo(
					FilterConfig.DINING_USER,
					"dining/dining_main",
					"dining_list_fm",
					"diningList"
				);
				modelUtils.setFilteringInfo(model, FilterConfig.DINING_USER);
		
		
		return "dining/dining_main";
	}
	
	@GetMapping("/user/dining_detail")
	public String diningDetail(@RequestParam("diningId") int diningId, Model model) {
		// 1. 다이닝 정보 조회 (서비스/매퍼 필요)
		DiningDomain diningInfo = ds.searchOneDining(diningId); // 예시
		List<RepMenuDomain> repMenuList = ds.searchRepMenu(diningId);
		
		Map<String, List<RepMenuDomain>> groupedMenuMap = repMenuList.stream()
        .collect(Collectors.groupingBy(RepMenuDomain::getDescription));

		// 2. 모델에 담기
		model.addAttribute("diningInfo", diningInfo);
		// model.addAttribute("repMenuList", repMenuList);
		model.addAttribute("groupedMenuMap", groupedMenuMap);

		// 3. 뷰 반환
		return "dining/dining_detail";
	}
	

	@GetMapping("/admin/admin_dining_detail")
	public String adminDiningDetail(Model model) {
		
		return "dining/admin_dining_detail_editor";
	}

    @DeleteMapping("/admin/dining/{diningId}")
    public ResponseEntity<Void> deleteDining(@PathVariable("diningId") int diningId) {
        try {
            ds.deleteDining(diningId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/admin/dining/edit")
    public String editDiningForm(@RequestParam int diningId, 
                                 @RequestParam(value = "message", required = false) String message, 
                                 Model model) {
        DiningDomain dining = ds.searchOneDining(diningId);
        System.out.println(dining);
        model.addAttribute("dining", dining);
        model.addAttribute("message", message);
        return "admin_dining/admin_dining_edit";
    }

    @PostMapping("/admin/dining/edit")
    public String editDiningSubmit(@ModelAttribute DiningDTO dto) {
        // 일반 데이터만 처리
        ds.updateDiningData(dto);
        return "redirect:/admin/dining/edit?diningId=" + dto.getDining_id() + "&message=dataUpdated";
    }

    @PostMapping("/admin/dining/editFiles")
    @ResponseBody // AJAX 업로드 시 필요
    public Map<String, Object> editDiningFiles(
        @RequestParam int dining_id,
        @RequestParam(value = "main_image_file", required = false) MultipartFile mainImage,
        @RequestParam(value = "logo_image_file", required = false) MultipartFile logoImage,
        @RequestParam(value = "carousel_image_file", required = false) MultipartFile carouselImage,
        @RequestParam(value = "carousel_index", required = false) Integer carouselIndex
    ) {
        // 파일 업로드만 처리
        String imageUrl = ds.updateDiningFiles(dining_id, mainImage, logoImage, carouselImage, carouselIndex);
        Map<String, Object> result = new HashMap<>();
        result.put("imageUrl", imageUrl);
        return result;
    }

    @PostMapping("/admin/dining/updateCarouselOrder")
    @ResponseBody
    public ResponseEntity<?> updateCarouselOrder(
        @RequestParam("dining_id") int diningId,
        @RequestParam("image_urls") List<String> imageUrls,
        @RequestParam(value = "deleted_image_urls", required = false) List<String> deletedImageUrls
    ) {
        System.out.println("[Controller] imageUrls: " + imageUrls);
        System.out.println("[Controller] deletedImageUrls: " + deletedImageUrls);
        try {
            ds.updateCarouselOrder(diningId, imageUrls, deletedImageUrls);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("순서 저장 실패: " + e.getMessage());
        }
    }
	
    @GetMapping("/admin/dining/add")
    public String addDiningForm(Model model) {
        model.addAttribute("dining", new kr.co.sist.dining.user.DiningDTO());
        return "admin_dining/admin_dining_add";
    }

    @PostMapping("/admin/dining/add")
    @ResponseBody
    public Map<String, Object> addDiningSubmit(@ModelAttribute kr.co.sist.dining.user.DiningDTO dto) {
        int newId = ds.insertDiningAndReturnId(dto);
        return Map.of("dining_id", newId);
    }
}
