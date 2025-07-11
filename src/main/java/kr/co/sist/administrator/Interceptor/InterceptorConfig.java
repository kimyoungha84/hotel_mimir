package kr.co.sist.administrator.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{

	@Autowired
	private AdminInterceptor ai;

	@Value("${admin.addPath}")
	private String addPath;
	@Value("${admin.excludePath}")
	private String[] excludePath;
	
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println(addPath);
		System.out.println(excludePath);
		
		registry.addInterceptor(ai)
		.addPathPatterns(addPath)
		.excludePathPatterns(excludePath);
	}//addInterceptors
	
	
}//class
