package kr.co.sist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${upload.saveDir}")
	private String uploadDir;

	/**
	 * ResourceHandlerRegistry 클래스로 정적리소스( 파일, 이미지 )를 jar파일 외부에 저장하고
	 * jar파일 내부로 경로를 전달하는 일
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/room_images/**")//URL의 경로
		.addResourceLocations("file:"+uploadDir+"/");//HDD의 경로
	}//addResourceHandlers
	
	
	
}//class
