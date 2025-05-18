package com.example.serviceimpl;



import java.io.IOException;
import java.nio.file.Files;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.service.EmailService;
import com.exampledto.EmailDTO;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String fromEmail;


    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private ResourceLoader resourceLoader;

    public void sendMail(EmailDTO emailDTO) {
        try {
            String templateName = decideTemplate(emailDTO); 
            String content = loadTemplate(templateName, emailDTO.getFirstName());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailDTO.getTo());
            helper.setSubject(emailDTO.getSubject());

            if (emailDTO.getAttachmentData() != null && emailDTO.getAttachmentName() != null) {
                helper.addAttachment(
                    emailDTO.getAttachmentName(),
                    new ByteArrayDataSource(emailDTO.getAttachmentData(), "application/pdf")
                );
            }

            helper.setText(content, true);
            mailSender.send(message);

            log.info("Email sent successfully to {}", emailDTO.getTo());
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", emailDTO.getTo(), e.getMessage());
        }
    }


    
    private String loadTemplate(String templateName, String recipientName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/" + templateName);
        String template = new String(Files.readAllBytes(resource.getFile().toPath()));
        return template.replace("${name}", recipientName);
    }
    
    private String decideTemplate(EmailDTO emailDTO) {
    	if(emailDTO.getSubject().contains("Sanction")) {
    		return "sanction-status-email.html";
    	}
        if (emailDTO.getSubject().contains("Eligible")) {
            return "enquiry-Approved-email.html";
        } else if (emailDTO.getSubject().contains("Welcome")) {
            return "welcome-email-template.html";
        } else if(emailDTO.getSubject().contains("InProcess")){
        	return "enquiry-inprocess-email.html";
        }else {
        	return "enquiry-rejection-email.html";
        }
    }

    

    


	

}
