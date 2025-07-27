package kr.co.sist.administrator.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.sist.administrator.AdminService;

@Component
public class PageAuthorityInterceptor implements HandlerInterceptor{

	@Autowired(required = false)
	private AdminService as;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean flag=false;
		boolean chkAuthStatus=false;
		
		HttpSession session=request.getSession();
		String sessionIdStr= (String)session.getAttribute("session_id");
		
		
		
		//String inputUri=
		/*접근 URL 받아오기*/
		String url=request.getRequestURI();
		System.out.println("\n\n\n\n\n\n>>> [PageInterceptor] uri: " + url);
		System.out.println("📌 Interceptor preHandle URL: " + request.getRequestURL());
		System.out.println("📌 Interceptor preHandle URI: " + request.getRequestURI());

		//Map으로 권한하고 mapping을 해서 그걸로 비교하는 method를 작성해야 함.
		
		chkAuthStatus=as.chkHaveAuthority(sessionIdStr, url);
		
		System.out.println("checkauthoroitystats --================================-----"+chkAuthStatus);
		
		
		
		if(!chkAuthStatus) {
			//권한이 없으니까 보내야지.
			response.sendRedirect("/admin/noAuthor"); //이거 때문에, 이 요청은 여기서 끝난것.
		}else {
			flag=true;
		}//end if~else
		
		
		return flag;
	}//preHandle

	

	
}//class
