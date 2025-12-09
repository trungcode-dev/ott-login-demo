package com.example.onetimetoken;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OttSuccessHandler.class);
    private final OneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");
    private final EmailService emailService;

    public OttSuccessHandler(EmailService emailService) {
        this.emailService = emailService;
    }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {


        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .path("/login/ott")
                .queryParam("token", oneTimeToken.getTokenValue());


        String magicLink = builder.toUriString();


        log.info("Generated Magic Link for user {}: {}", oneTimeToken.getUsername(), magicLink);


        var body = """
Hello, from Spring Security!
Below you will find your magic link to login to our super secret application!


%s
""".formatted(magicLink);


        try {
            var sendTo = oneTimeToken.getUsername();
            log.info("Sending One Time Token to username: {}", sendTo);
            emailService.sendEmail(
                    getEmail(sendTo),
                    "One Time Token Login",
                    body
            );
        } catch (IOException e) {
// don't block the request on email failure; log and continue redirect to sent page
            log.error("Failed to send magic link email: {}", e.getMessage(), e);
        }


        this.redirectHandler.handle(request, response, oneTimeToken);
    }
    private String getEmail(String username) {
// replace with actual lookup if needed
        log.debug("Retrieving email for user: {}", username);
        return "trunglhth04669@fpt.edu.vn";
    }
}