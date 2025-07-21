package kr.co.sist.administrator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.sist.util.ModelUtils;

@Component
public class AdministratorSendMail {

	@Autowired
	private final ModelUtils modelUtils;

	@Autowired
	private JavaMailSender mailSender;

    AdministratorSendMail(ModelUtils modelUtils) {
        this.modelUtils = modelUtils;
    }
	
	public void sendMail(String to, String subject,String url) throws MessagingException{
		MimeMessage message=mailSender.createMimeMessage();
		
		MimeMessageHelper helper=new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(loadHtmlMailTemplate(url), true);
		
		//helper.setFrom("hyeon931023@gmail.com");//보내는 사람 설정
		
		
		mailSender.send(message);
	}//sendHtmlMail
	
	
	
	/**
	 * html 템플릿 호출 및 반환<br>
	 * 아니야 ... html 템플릿 보낼거야....
	 * @return
	 * @throws IOException
	 */
	public String loadHtmlMailTemplate(String url){
		ClassPathResource resource =new ClassPathResource(url);
		String StreamUtilStr="";
		
		try {
			StreamUtilStr=StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}//try~catch
		
		return StreamUtilStr;
	}//loadHtmlMailTemplate
	
	

	
}//class
