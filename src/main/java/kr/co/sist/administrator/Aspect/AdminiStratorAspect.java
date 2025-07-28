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
		
		System.out.println("sldkfjl------------------------------"+jp.getSignature().toString());
		
		if(request.getRequestURL().toString().contains("/admin/employee")) {
			logger.info("[직원] 직원관리 접근");	
		}else if(request.getRequestURL().toString().contains("/admin/member")) {
			logger.info("[회원] 회원 관리 접근");
		}else if(request.getRequestURL().toString().contains("/admin/roomlist")) {
			logger.info("[객실] 정보 접근");
		}else if(request.getRequestURL().toString().contains("/admin/resvroomlist")) {
			logger.info("[객실] 예약 현황 접근");
		}else if(request.getRequestURL().toString().contains("/admin/roomsales")) {
			logger.info("[객실] 매출 현황 접근");
		}else if(request.getRequestURL().toString().contains("/admin/dining")) {
			logger.info("[다이닝] 다이닝 관리 접근");
		}else if(request.getRequestURL().toString().contains("/admin/adminDiningResvList")) {
			logger.info("[다이닝] 예약 관리 접근");
		}else if(request.getRequestURL().toString().contains("/admin/adminDiningResvSlot")) {
			logger.info("[다이닝] 좌석 관리 접근");
		}else if(request.getRequestURL().toString().contains("/admin/faq")) {
			logger.info("[문의] FAQ 접근");
		}else if(request.getRequestURL().toString().contains("/admin/chat")) {
			logger.info("[문의] 실시간 문의 접근");
		}else if(request.getRequestURL().toString().contains("/admin/employee/log")) {
			logger.info("[관리자] 로그 기록 접근");
		}
		
		
		
		
	}//beforeAdvice
	
	
	@After("execution(* kr.co.sist.administrator.*.*(..))")
	public void afterAdvice(JoinPoint jp) {
		//logger.info("여기는 after "+jp.getSignature());
		ServletRequestAttributes sra=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	if(sra == null) {return;}//비웹 요청일 경우
		
		HttpServletRequest request=sra.getRequest();
		HttpSession session=request.getSession(false);//없으면 null 반환
		
		
		if(request.getRequestURL().toString().contains("/admin/employee")) {
			logger.info("[직원] 직원관리 접근 완료");	
		}else if(request.getRequestURL().toString().contains("/admin/member")) {
			logger.info("[회원] 회원 관리 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/roomlist")) {
			logger.info("[객실] 정보 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/resvroomlist")) {
			logger.info("[객실] 예약 현황 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/roomsales")) {
			logger.info("[객실] 매출 현황 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/dining")) {
			logger.info("[다이닝] 다이닝 관리 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/adminDiningResvList")) {
			logger.info("[다이닝] 예약 관리 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/adminDiningResvSlot")) {
			logger.info("[다이닝] 좌석 관리 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/faq")) {
			logger.info("[문의] FAQ 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/chat")) {
			logger.info("[문의] 실시간 문의 접근 완료");
		}else if(request.getRequestURL().toString().contains("/admin/employee/log")) {
			logger.info("[관리자] 로그 기록 접근 완료");
		}
		
		
		
	}//afterAdvice
	
	
	
	
	@AfterThrowing(
			pointcut="execution(* kr.co.sist.administrator.*.*(..))",
			throwing="ex"
			)
	public void afterThrowAdvice(JoinPoint jp, Throwable ex) {
		//System.out.println(ex.getMessage());
		//logger.info("여기는 afterthrow "+jp.getSignature());
		//jp.getSignature().toString().contains("/admin/adminDiningResvSlot")
		ServletRequestAttributes sra=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(sra == null) {return;}//비웹 요청일 경우
		
		HttpServletRequest request=sra.getRequest();
		HttpSession session=request.getSession(false);//없으면 null 반환
		
		if(request.getRequestURL().toString().contains("/admin/employee")) {
			logger.info("[직원] 직원관리 접근 예외!!!! 터짐");	
		}else if(request.getRequestURL().toString().contains("/admin/member")) {
			logger.info("[회원] 회원 관리 접근  예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/roomlist")) {
			logger.info("[객실] 정보 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/resvroomlist")) {
			logger.info("[객실] 예약 현황 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/roomsales")) {
			logger.info("[객실] 매출 현황 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/dining")) {
			logger.info("[다이닝] 다이닝 관리 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/adminDiningResvList")) {
			logger.info("[다이닝] 예약 관리 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/adminDiningResvSlot")) {
			logger.info("[다이닝] 좌석 관리 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/faq")) {
			logger.info("[문의] FAQ 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/chat")) {
			logger.info("[문의] 실시간 문의 접근 예외!!!! 터짐");
		}else if(request.getRequestURL().toString().contains("/admin/employee/log")) {
			logger.info("[관리자] 로그 기록 접근 예외!!!! 터짐");
		}
		
		
		
		
	}//beforeAdvice
	
}//class
