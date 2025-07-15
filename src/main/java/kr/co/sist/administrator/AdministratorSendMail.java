package kr.co.sist.administrator;
import kr.co.sist.util.ModelUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class AdministratorSendMail {

    private final ModelUtils modelUtils;

	@Autowired
	private JavaMailSender mailSender;

    AdministratorSendMail(ModelUtils modelUtils) {
        this.modelUtils = modelUtils;
    }
	
	public void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException{
		MimeMessage message=mailSender.createMimeMessage();
		
		MimeMessageHelper helper=new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);
		
		helper.setFrom("hyeon931023@gmail.com");//보내는 사람 설정
		
		
		mailSender.send(message);
	}//sendHtmlMail
	
	
	
	/**
	 * html 템플릿 호출 및 반환
	 * @return
	 * @throws IOException
	 */
	public String loadHtmlMailTemplate() throws IOException{
		ClassPathResource resource =new ClassPathResource("templates/administrator_email_template/reset_password.html");
		
		return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
	}//loadHtmlMailTemplate
	
	
}//class
