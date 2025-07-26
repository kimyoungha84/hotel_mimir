package kr.co.sist.dining.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import kr.co.sist.dining.repmenu.RepMenuService;
import kr.co.sist.dining.user.RepMenuDomain;
import kr.co.sist.dining.user.RepMenuDTO;

@Controller
public class RepMenuController {

	@Autowired
	private RepMenuService repMenuService;

	@GetMapping("/admin/repMenu")
	public String adminRepMenu(@RequestParam("diningId") int diningId, Model model) {
		// 해당 다이닝의 대표메뉴 목록 조회
		List<RepMenuDomain> repMenuList = repMenuService.getRepMenuListByDiningId(diningId);
		// 특정 다이닝의 분류 목록 조회 (기존 분류들을 셀렉터에 표시용)
		List<String> descriptions = repMenuService.getDistinctDescriptionsByDiningId(diningId);
		// 모델에 데이터 추가
		model.addAttribute("repMenuList", repMenuList);
		model.addAttribute("diningId", diningId);
		model.addAttribute("descriptions", descriptions);
		return "admin_dining/admin_dining_rep_menu";
	}

	// 대표메뉴 추가 처리 (AJAX 요청용)
	@PostMapping("/api/admin/repMenu/add")
	@ResponseBody
	public Map<String, Object> addRepMenu(@RequestBody RepMenuDTO repMenuDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			boolean result = repMenuService.addRepMenu(repMenuDTO);
			if (result) {
				response.put("success", true);
				response.put("message", "대표메뉴가 성공적으로 추가되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "대표메뉴 추가에 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "대표메뉴 추가 중 오류가 발생했습니다: " + e.getMessage());
		}
		return response;
	}

	// 대표메뉴 수정 처리 (AJAX 요청용)
	@PutMapping("/api/admin/repMenu/update")
	@ResponseBody
	public Map<String, Object> updateRepMenu(@RequestBody RepMenuDTO repMenuDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			boolean result = repMenuService.updateRepMenu(repMenuDTO);
			if (result) {
				response.put("success", true);
				response.put("message", "대표메뉴가 성공적으로 수정되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "수정된 대표메뉴가 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "대표메뉴 수정 중 오류가 발생했습니다: " + e.getMessage());
		}
		return response;
	}

	// 대표메뉴 삭제 처리 (AJAX 요청용)
	@DeleteMapping("/api/admin/repMenu/{menuId}")
	@ResponseBody
	public Map<String, Object> deleteRepMenu(@PathVariable("menuId") int menuId) {
		Map<String, Object> response = new HashMap<>();
		try {
			boolean result = repMenuService.deleteRepMenu(menuId);
			if (result) {
				response.put("success", true);
				response.put("message", "대표메뉴가 성공적으로 삭제되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "삭제할 대표메뉴가 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "대표메뉴 삭제 중 오류가 발생했습니다: " + e.getMessage());
		}
		return response;
	}
}
