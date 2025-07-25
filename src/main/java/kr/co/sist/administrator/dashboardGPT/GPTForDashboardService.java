package kr.co.sist.administrator.dashboardGPT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class GPTForDashboardService{

 
		@Autowired(required = false)
	    private RssService rss;

		@Value("${weather.url}")
		private String weatherUrl;
		
		@Value("${news.url}")
		private String newsUrl;
		
		@Autowired(required = false)
		private UseChatGPTForStatistics ugs;
		
	   // @Scheduled(cron = "0 0 11 * * *")  // 매일 오전 9시에 실행
	    public String callGPTbySchedule() throws Exception {
	    	String weather="", news="";
	    	StringBuilder sbPromptStr=new StringBuilder();
	    	
	    	  	
	        // 날씨 정보 수집
	        weather = rss.getTodayForecast(weatherUrl);

	        // 뉴스 정보 수집
	        news = rss.getLatestNews(newsUrl);

	        // GPT 프롬프트 생성
	        // GPT API에 요청 보내고 응답을 처리 (API 호출 부분)
	        // 예시: gptApiService.sendRequest(prompt);
	        StringBuilder sbWeather=new StringBuilder();
	        sbWeather.append("당신은 5성급 호텔의 셰프입니다.\n")
	        .append("주어진 날씨 데이터를 기반으로 어떤 음식이 많이 팔릴지 예상해주세요. 이때 질문을 요약하고 이에 대한 답변 형식으로 출력할니다.")
	        .append("정중히 말하되, 친숙하게 친근하게 말해주세요");
	        
	        sbPromptStr.append(ugs.analyzeUseGPT("이건 날씨 RSS입니다.\n"+weather+sbWeather.toString())).append("\n");  
	      
	        sbPromptStr.append("\n\n");
	        
	        StringBuilder sbNews=new StringBuilder();
	        sbNews.append("당신은 경제 전문가입니다.\n")
	        .append("물가와 관련된 정보 등을 이용해, 사람들의 소비 심리를 1줄로 추측해주세요.");
	        sbPromptStr.append(ugs.analyzeUseGPT("이 데이터는 경제뉴스 RSS입니다.\n"+news+sbNews.toString())).append("\n");
	        
	        return sbPromptStr.toString();
	    }//collectAndProcessData
	
}//class
