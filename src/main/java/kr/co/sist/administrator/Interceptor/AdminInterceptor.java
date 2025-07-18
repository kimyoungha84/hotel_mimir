package kr.co.sist.administrator.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminInterceptor implements HandlerInterceptor{
	
	
	/*여기서 session있으면 통과 없으면 이동시키면 되지.*/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean flag=false;
		/*session_id 받아오기*/
		HttpSession session=request.getSession(false);//기존에 session이 없으면 null 출력

		
		if(session == null || session.getAttribute("session_id") == null) {
//			System.out.println("----------------------------AdminInterceptor 수정--------------------------------------------");
			//세션아이디가 없으니까, 로그인 페이지로 이동
			response.sendRedirect("/admin/login"); 
		}else {
	
			flag=true; //원래 요청 흐름대로 컨트롤러 실행
		}//end if~else
		
		return flag;
	}//preHandle


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		HttpSession session=request.getSession(false);
		
		if(modelAndView != null && session !=null) {
			modelAndView.addObject("session_id",request.getSession().getAttribute("session_id"));
			modelAndView.addObject("session_name",request.getSession().getAttribute("session_name"));			
		}//end if
	}//postHandle
	
	
	
	
	
	
}//class
