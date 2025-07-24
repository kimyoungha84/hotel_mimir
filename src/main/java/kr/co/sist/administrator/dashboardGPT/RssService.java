package kr.co.sist.administrator.dashboardGPT;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RssService {

	//날씨 정보
    public String getTodayForecast(String weatherURL) throws Exception {
        String url = weatherURL;
        Document doc = Jsoup.connect(url).get();

        Element forecast = doc.selectFirst("location > data > wf");
        
        
        System.out.println("날씨"+forecast.text());
        return forecast != null ? forecast.text() : "날씨 정보를 불러올 수 없습니다.";
    }//getTodayForecast
    
    //뉴스 정보
    public String getLatestNews(String newsURL) throws Exception {
        String url = newsURL ; // SBS 뉴스 예시
        Document doc = Jsoup.connect(url).get();

        Elements items = doc.select("item");
        StringBuilder newsBuilder = new StringBuilder();

        for (Element item : items) {
            String title = item.select("title").text();
            String description = item.select("description").text();
            newsBuilder.append(title).append(": ").append(description).append("\n\n");
        }//end for

        System.out.println("뉴스으으----"+newsBuilder.toString());
        return newsBuilder.toString();
    }//getTodayForecast
    
    

    
}//class
