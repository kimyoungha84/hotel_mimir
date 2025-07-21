package kr.co.sist.dining.user;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class DiningDTO {
	private int dining_id;
	private String dining_name;
	private String type;
	private String location;
	private String phone_number;
	private String dining_operation_hours;
	private String manager_name;
	private Date diningRegDate;
	private String dining_introduction;
	private String dining_detailinfo;
	
	// Y / N으로 저장
	private String dining_resv_availability;
	private String dining_classification;

	// 이미지 업로드용
	private MultipartFile main_image_file;
	private MultipartFile logo_image_file;
	private List<MultipartFile> carousel_image_files;
	
	
	

}
