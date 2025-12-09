package com.example.onetimetoken;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${sendgrid.api-key:}")
    private String sendgridApiKey;

    /**
     * Send a plain text email using SendGrid. If API key is empty, method will only log (dev mode).
     */
    public void sendEmail(String to, String subject, String content) throws IOException {
        if (sendgridApiKey == null || sendgridApiKey.isBlank()) {
            log.warn("SendGrid API key is empty — skipping send. To enable real sending, set SENDGRID_API_KEY env var.");
            log.info("Simulated send -> to: {}, subject: {}, content: {}", to, subject, content.replaceAll("\\n", " "));
            return;
        }
        Email from = new Email("trungly498@gmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, toEmail, emailContent);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            log.info("SendGrid status: {}", response.getStatusCode());
            if (response.getStatusCode() >= 400) {
                log.warn("SendGrid response body: {}", response.getBody());
            } else {
                log.debug("SendGrid body: {}", response.getBody());
            }
        } catch (IOException ex) {
            log.error("Failed to send email via SendGrid: {}", ex.getMessage(), ex);
            throw new IOException("Failed to send email: " + ex.getMessage(), ex);
        }
    }
}