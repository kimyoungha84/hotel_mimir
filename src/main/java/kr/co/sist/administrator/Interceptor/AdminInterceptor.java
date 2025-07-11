package kr.co.sist.administrator.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor{

	/*여기서 session있으면 통과 없으면 이동시키면 되지.*/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean flag=false;
		/*session_id 받아오기*/
		String session_id=(String)request.getSession().getAttribute("session_id");
		//System.out.println("prehandler session_id-------"+session_id);
		

		
		if(session_id == null) {
			//세션아이디가 없으니까, 로그인 페이지로 이동
			response.sendRedirect("/admin/login"); 
		}else {
			/*세션 아이디가 있을 때는, 관리자 첫페이지로 이동*/
			flag=true; //원래 요청 흐름대로 컨트롤러 실행
		}//end if~else
		
		return flag;
	}//preHandle
	
	
}//class
