package kr.co.sist.administrator.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PageInterceptorConfig implements WebMvcConfigurer{

	
	@Autowired
	private PageAuthorityInterceptor pai;
	

	@Value("${admin.page.addPath}")
	private String[] addPagePath;


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(pai)
		.addPathPatterns(addPagePath)
		.order(2);
	}//addInterceptors
	
	
	
}//class
