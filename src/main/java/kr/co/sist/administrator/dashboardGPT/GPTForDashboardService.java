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
	    public void callGPTbySchedule() throws Exception {
	    	String weather="", news="", prompt="", resultStr="";
	    	
	    	    	
	        // 날씨 정보 수집
	        weather = rss.getTodayForecast(weatherUrl);

	        // 뉴스 정보 수집
	        news = rss.getLatestNews(newsUrl);

	        // GPT 프롬프트 생성
	        // GPT API에 요청 보내고 응답을 처리 (API 호출 부분)
	        // 예시: gptApiService.sendRequest(prompt);
	        StringBuilder sbWeather=new StringBuilder();
	        sbWeather.append("너는 각 계절의 야채, 과일을 정확히 알고 있는 요리사야.\n")
	        .append("그걸 토대로 이 날씨에는 농작물이 자라지 않을 가능성이 높으니, 식자재를 바꿔주세요와 같은 느낌으로 1줄로 정중하게 출력해주세요.");
	        resultStr=ugs.analyzeUseGPT(weather+sbWeather.toString());
	        
	      
//	        System.out.println(resultStr);
	        StringBuilder sbNews=new StringBuilder();
	        sbNews.append("너는 경제학자야.\n")
	        .append("물가 상승률 등을 고려해서 외식이 많아질 것 같은지를 1문장으로 예측한 결과를 정중하게 출력해주세요.");
	        resultStr=ugs.analyzeUseGPT(news+sbNews.toString());
	//        System.out.println();
	        
	    }//collectAndProcessData
	
}//class
