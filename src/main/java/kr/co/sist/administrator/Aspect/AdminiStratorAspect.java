package kr.co.sist.administrator.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AdminiStratorAspect {
	
	private static final Logger logger=LoggerFactory.getLogger(AdminiStratorAspect.class);
	
	

	@Before("execution(* kr.co.sist.administrator.*.*(..))")
	public void beforeAdvice(JoinPoint jp) {
		//session을 가져오기위한 코드
		ServletRequestAttributes sra=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(sra == null) {return;}//비웹 요청일 경우
		
		HttpServletRequest request=sra.getRequest();
		HttpSession session=request.getSession(false);//없으면 null 반환
		logger.info("접근 URL -------------------"+request.getRequestURL());
		String idname="";
		
		if(session==null) {
			idname="anonymos";
		}else {
			idname=(String)session.getAttribute("session_id");
			MDC.put("userId", idname);
		}
				
		
		
		//만약 //ㅍtring kr.co.sist.administrator.AdministratorRESTController.loginChk(LoginDTO,HttpSession,HttpServletRequest)를 쓰면 이걸 로그에 남깁시다.
		if(jp.getSignature().toString().contains("loginChk")) {
			//그러면 지금 login 시도가 일어나고 있다는 의미지
			logger.info("로그인 시도-------------"+jp.getArgs()[0].toString());
		}//end
		
		if(jp.getSignature().toString().contains("loginout")) {
			MDC.clear();	
		}//end
		
		
	    //Object[] args = jp.getArgs();
	    //for (Object arg : args) {
	    //    logger.info("arg: " + arg);
	    //}
		
		//logger.info("여기는 before---idname---"+idname);
		//logger.info("여기는 before "+jp.getSignature());
		
		
	}//beforeAdvice
	
	
	@After("execution(* kr.co.sist.administrator.*.*(..))")
	public void afterAdvice(JoinPoint jp) {
		logger.info("여기는 after "+jp.getSignature());
	}//afterAdvice
	
	@AfterThrowing(
			pointcut="execution(* kr.co.sist.administrator.*.*(..))",
			throwing="ex"
			)
	public void afterThrowAdvice(JoinPoint jp, Throwable ex) {
		//System.out.println(ex.getMessage());
		logger.info("여기는 afterthrow "+jp.getSignature());
		
	}//beforeAdvice
	
}//class
